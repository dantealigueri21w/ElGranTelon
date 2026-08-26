-- Esquema real de Room, exportado por Gradle (app/schemas/pe.appmobile.elgrantelon.data.AppDatabase/1.json)
-- version 1

CREATE TABLE IF NOT EXISTS `perfil` (`id` INTEGER NOT NULL, `alias` TEXT NOT NULL, `avatarId` INTEGER NOT NULL, PRIMARY KEY(`id`));

CREATE TABLE IF NOT EXISTS `acto` (`id` INTEGER NOT NULL, `nombre` TEXT NOT NULL, `orden` INTEGER NOT NULL, `desbloqueado` INTEGER NOT NULL, `completado` INTEGER NOT NULL, PRIMARY KEY(`id`));

CREATE TABLE IF NOT EXISTS `poema` (`id` INTEGER NOT NULL, `actoId` INTEGER NOT NULL, `titulo` TEXT NOT NULL, `autor` TEXT NOT NULL, `texto` TEXT NOT NULL, `marcasPausaCsv` TEXT NOT NULL, `volumenMinimo` REAL NOT NULL, `volumenMaximo` REAL NOT NULL, `ritmoMinimoSilabasPorMinuto` INTEGER NOT NULL, `ritmoMaximoSilabasPorMinuto` INTEGER NOT NULL, `dominado` INTEGER NOT NULL, PRIMARY KEY(`id`));

CREATE TABLE IF NOT EXISTS `intento_declamacion` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `poemaId` INTEGER NOT NULL, `fechaEpochDay` INTEGER NOT NULL, `volumenPromedio` REAL NOT NULL, `volumenAdecuado` INTEGER NOT NULL, `entonacionAdecuada` INTEGER NOT NULL, `silabasPorMinuto` INTEGER NOT NULL, `ritmoAdecuado` INTEGER NOT NULL, `pausasRespetadas` INTEGER NOT NULL, `pausasEsperadas` INTEGER NOT NULL, `aprobado` INTEGER NOT NULL, `contornoTonoCsv` TEXT NOT NULL, `esRepaso` INTEGER NOT NULL);

CREATE TABLE IF NOT EXISTS `cartel` (`poemaId` INTEGER NOT NULL, `fechaObtencionEpochDay` INTEGER NOT NULL, `descripcionLogro` TEXT NOT NULL, PRIMARY KEY(`poemaId`));

CREATE TABLE IF NOT EXISTS `medalla` (`id` TEXT NOT NULL, `fechaObtencionEpochDay` INTEGER NOT NULL, PRIMARY KEY(`id`));

CREATE TABLE IF NOT EXISTS `racha` (`id` INTEGER NOT NULL, `diasConsecutivos` INTEGER NOT NULL, `ultimaFechaEpochDay` INTEGER NOT NULL, PRIMARY KEY(`id`));
