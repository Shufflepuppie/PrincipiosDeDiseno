/* =========================================================
   Script de base de datos - CacheDemo
   Se puede ejecutar varias veces: solo crea lo que falta.
   Ejecutar completo en SSMS (F5).
   ========================================================= */

-- 1. Base de datos
IF DB_ID('CacheDemo') IS NULL
    CREATE DATABASE CacheDemo;
GO

USE CacheDemo;
GO

-- 2. Tabla
IF OBJECT_ID('dbo.producto', 'U') IS NULL
    CREATE TABLE producto (
        id        INT IDENTITY(1,1) PRIMARY KEY,
        nombre    NVARCHAR(100) NOT NULL,
        categoria NVARCHAR(50)  NOT NULL,
        precio    DECIMAL(10,2) NOT NULL
    );
GO

-- 3. 100 000 registros de prueba (solo si la tabla está vacía)
IF NOT EXISTS (SELECT 1 FROM producto)
BEGIN
    SET NOCOUNT ON;
    DECLARE @i INT = 1;

    BEGIN TRAN;
    WHILE @i <= 100000
    BEGIN
        INSERT INTO producto (nombre, categoria, precio)
        VALUES (
            CONCAT(N'Producto ', @i),
            CHOOSE(@i % 5 + 1, N'Electrónica', N'Hogar', N'Ropa', N'Deportes', N'Libros'),
            CAST(RAND(CHECKSUM(NEWID())) * 500 + 1 AS DECIMAL(10,2))
        );
        SET @i += 1;
    END;
    COMMIT;
END;
GO

-- 4. Usuario que usa la aplicación Spring (solo lectura)
USE master;
GO

IF SUSER_ID('cachedemo_app') IS NULL
    CREATE LOGIN cachedemo_app WITH PASSWORD = 'Cache123!';
GO

USE CacheDemo;
GO

IF USER_ID('cachedemo_app') IS NULL
    CREATE USER cachedemo_app FOR LOGIN cachedemo_app;
GO

ALTER ROLE db_datareader ADD MEMBER cachedemo_app;
GO

-- 5. Verificación
SELECT COUNT(*) AS total FROM producto;   -- debe dar 100000
GO
