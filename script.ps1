$ErrorActionPreference = "Stop"
$base = "http://localhost:8080"
$script:fallos = 0

function Probar-Api {
    param(
        [string]$Nombre,
        [string]$Metodo,
        [string]$Url,
        [hashtable]$Headers = @{},
        $Body = $null,
        [int[]]$Esperados = @(200)
    )

    $parametros = @{
        Uri             = $Url
        Method          = $Metodo
        Headers         = $Headers
        UseBasicParsing = $true
        ErrorAction     = "Stop"
    }

    if ($null -ne $Body) {
        $json = ConvertTo-Json -InputObject $Body -Depth 10 -Compress
        $parametros.ContentType = "application/json; charset=utf-8"
        $parametros.Body = [System.Text.Encoding]::UTF8.GetBytes($json)
    }

    $estado = 0
    $contenido = ""

    try {
        $respuesta = Invoke-WebRequest @parametros
        $estado = [int]$respuesta.StatusCode
        $contenido = $respuesta.Content
    }
    catch {
        $respuestaError = $_.Exception.Response

        if ($respuestaError) {
            $estado = [int]$respuestaError.StatusCode

            try {
                $lector = New-Object System.IO.StreamReader(
                    $respuestaError.GetResponseStream(),
                    [System.Text.Encoding]::UTF8
                )
                $contenido = $lector.ReadToEnd()
                $lector.Dispose()
            }
            catch {
                $contenido = $_.Exception.Message
            }
        }
        else {
            $contenido = $_.Exception.Message
        }
    }

    $correcto = $Esperados -contains $estado

    if ($correcto) {
        Write-Host "OK     $Nombre - HTTP $estado" -ForegroundColor Green
    }
    else {
        $script:fallos++
        Write-Host "FALLÓ  $Nombre - HTTP $estado - $contenido" -ForegroundColor Red
    }

    $datos = $null

    if ($contenido) {
        try {
            $datos = ConvertFrom-Json -InputObject $contenido
        }
        catch {
            $datos = $contenido
        }
    }

    return [PSCustomObject]@{
        Correcto = $correcto
        Estado   = $estado
        Datos    = $datos
    }
}

function Obtener-Token {
    param(
        [string]$Correo,
        [string]$Nombre
    )

    $claveSegura = Read-Host "Contraseña de $Nombre" -AsSecureString
    $clave = (New-Object System.Net.NetworkCredential("", $claveSegura)).Password

    $resultado = Probar-Api `
        -Nombre "POST /api/usuarios/login ($Nombre)" `
        -Metodo "POST" `
        -Url "$base/api/usuarios/login" `
        -Body @{
            correo     = $Correo
            contrasena = $clave
        }

    if (-not $resultado.Correcto -or -not $resultado.Datos.token) {
        throw "No se pudo iniciar sesión con la cuenta de $Nombre."
    }

    return $resultado.Datos
}

try {
    $paciente = Obtener-Token `
        -Correo "paciente@nutrivida.com" `
        -Nombre "paciente"

    $admin = Obtener-Token `
        -Correo "admin@nutrivida.com" `
        -Nombre "administrador"

    $idUsuario = [long]$paciente.usuario.idUsuario
    $cabecerasPaciente = @{
        Authorization = "Bearer $($paciente.token)"
    }
    $cabecerasAdmin = @{
        Authorization = "Bearer $($admin.token)"
    }

    Probar-Api `
        -Nombre "POST /api/usuarios/renovar" `
        -Metodo "POST" `
        -Url "$base/api/usuarios/renovar" `
        -Headers $cabecerasPaciente | Out-Null

    Probar-Api `
        -Nombre "GET /api/usuarios-personas/usuario/$idUsuario" `
        -Metodo "GET" `
        -Url "$base/api/usuarios-personas/usuario/$idUsuario" `
        -Headers $cabecerasPaciente `
        -Esperados @(200, 204) | Out-Null

    Probar-Api `
        -Nombre "GET /api/contactos/usuario/$idUsuario" `
        -Metodo "GET" `
        -Url "$base/api/contactos/usuario/$idUsuario" `
        -Headers $cabecerasPaciente `
        -Esperados @(200, 204) | Out-Null

    $tipos = Probar-Api `
        -Nombre "GET /api/contactos/tipos-asunto" `
        -Metodo "GET" `
        -Url "$base/api/contactos/tipos-asunto"

    $listaTipos = @($tipos.Datos)
    $primerTipo = $listaTipos |
        Where-Object { $null -ne $_.idTipoAsunto } |
        Select-Object -First 1

    if ($null -eq $primerTipo) {
        throw "No se encontró un tipo de asunto para probar el envío del contacto."
    }

    $mensaje = "Prueba temporal de conexión $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')"

    $creacion = Probar-Api `
        -Nombre "POST /api/contactos/usuario/$idUsuario" `
        -Metodo "POST" `
        -Url "$base/api/contactos/usuario/$idUsuario" `
        -Headers $cabecerasPaciente `
        -Body @{
            idUsuario    = $idUsuario
            idTipoAsunto = [long]$primerTipo.idTipoAsunto
            mensaje      = $mensaje
        } `
        -Esperados @(200, 201)

    if ($creacion.Correcto -and $creacion.Datos.idContacto) {
        $idContacto = [long]$creacion.Datos.idContacto

        Probar-Api `
            -Nombre "DELETE /api/contactos/$idContacto (limpieza de prueba)" `
            -Metodo "DELETE" `
            -Url "$base/api/contactos/$idContacto" `
            -Headers $cabecerasAdmin `
            -Esperados @(200, 204) | Out-Null
    }
    elseif ($creacion.Correcto) {
        Write-Host "El contacto se creó, pero la respuesta no incluyó idContacto para borrarlo." `
            -ForegroundColor Yellow
    }
}
catch {
    $script:fallos++
    Write-Host "ERROR DEL SCRIPT: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

if ($script:fallos -eq 0) {
    Write-Host "Todas las pruebas focalizadas pasaron." -ForegroundColor Green
}
else {
    Write-Host "Fallos encontrados: $script:fallos" -ForegroundColor Red
}