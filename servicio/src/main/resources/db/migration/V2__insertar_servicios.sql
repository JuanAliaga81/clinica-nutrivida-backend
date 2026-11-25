INSERT INTO servicio (
    tipo_servicio,
    nombre,
    descripcion,
    duracion,
    modalidad,
    profesional,
    precio
) VALUES
    (
        'Consulta',
        'Primera consulta nutricional',
        'Evaluación inicial: anamnesis, antropometría completa y diseño del primer plan alimenticio.',
        '50 min',
        'Presencial',
        'Nutricionista',
        35000.00
    ),
    (
        'Consulta',
        'Control nutricional (seguimiento)',
        'Seguimiento mensual: medición de indicadores y ajuste del plan vigente.',
        '30 min',
        'Presencial',
        'Nutricionista',
        25000.00
    ),
    (
        'Consulta',
        'Control nutricional quincenal',
        'Seguimiento intensivo cada 15 días. Recomendado en los primeros 2 meses.',
        '30 min',
        'Presencial',
        'Nutricionista',
        22000.00
    ),
    (
        'Consulta',
        'Teleconsulta nutricional',
        'Consulta de seguimiento vía videollamada. Requiere contar con consulta presencial previa.',
        '30 min',
        'Online (video)',
        'Nutricionista',
        20000.00
    ),
    (
        'Consulta',
        'Consulta de urgencia / reagendada',
        'Para pacientes que requieren atención fuera de su control habitual.',
        '30 min',
        'Presencial',
        'Nutricionista',
        28000.00
    ),
    (
        'Plan especializado',
        'Plan pérdida de peso (1 mes)',
        'Incluye primera consulta + 1 control quincenal + plan alimenticio personalizado + seguimiento por WhatsApp.',
        '—',
        'Presencial',
        'Nutricionista',
        65000.00
    ),
    (
        'Plan especializado',
        'Plan pérdida de peso (3 meses)',
        'Incluye primera consulta + 5 controles + 3 planes mensuales + seguimiento continuo.',
        '—',
        'Presencial',
        'Nutricionista',
        170000.00
    ),
    (
        'Plan especializado',
        'Plan nutrición deportiva (1 mes)',
        'Para deportistas y personas con actividad física frecuente. Cálculo de requerimientos energéticos y proteicos.',
        '—',
        'Presencial',
        'Nutricionista',
        70000.00
    ),
    (
        'Plan especializado',
        'Plan control diabetes / hipertensión',
        'Plan adaptado para patologías metabólicas. Coordinación con médico tratante si aplica.',
        '—',
        'Presencial',
        'Nutricionista',
        75000.00
    ),
    (
        'Plan especializado',
        'Plan alimentación vegetariana/vegana',
        'Diseñado para garantizar aporte adecuado de proteínas, hierro, vitamina B12 y calcio sin productos animales.',
        '—',
        'Presencial',
        'Nutricionista',
        68000.00
    ),
    (
        'Plan especializado',
        'Plan alimentación infantil (2-12 años)',
        'Evaluación nutricional pediátrica y diseño de plan adaptado a la etapa de desarrollo del niño.',
        '—',
        'Presencial',
        'Nutricionista',
        65000.00
    ),
    (
        'Evaluación',
        'Antropometría completa',
        'Peso, talla, IMC, circunferencia de cintura, cadera, brazo y % de grasa corporal con bioimpedanciometría.',
        '20 min',
        'Presencial',
        'Nutricionista',
        18000.00
    ),
    (
        'Evaluación',
        'Bioimpedanciometría',
        'Medición de composición corporal: masa grasa, masa muscular, agua corporal y edad metabólica.',
        '15 min',
        'Presencial',
        'Nutricionista',
        12000.00
    ),
    (
        'Evaluación',
        'Encuesta de hábitos alimentarios',
        'Análisis del patrón alimentario actual. Identificación de déficit y excesos nutricionales.',
        '20 min',
        'Presencial',
        'Nutricionista',
        10000.00
    ),
    (
        'Evaluación',
        'Análisis de exámenes de laboratorio',
        'Interpretación de hemograma, perfil bioquímico y lipídico en contexto nutricional.',
        '20 min',
        'Presencial',
        'Nutricionista',
        15000.00
    ),
    (
        'Taller grupal',
        'Taller de alimentación saludable',
        'Máx. 10 personas. Conceptos básicos de alimentación equilibrada y lectura de etiquetas.',
        '90 min',
        'Presencial (grupo)',
        'Nutricionista',
        15000.00
    ),
    (
        'Taller grupal',
        'Taller de cocina nutritiva',
        'Preparación de recetas saludables. Incluye degustación. Máx. 8 personas.',
        '120 min',
        'Presencial (grupo)',
        'Nutricionista',
        20000.00
    ),
    (
        'Taller grupal',
        'Taller nutrición para deportistas',
        'Hidratación, nutrición pre y post entrenamiento, suplementación básica. Máx. 12 personas.',
        '90 min',
        'Presencial (grupo)',
        'Nutricionista',
        18000.00
    );
