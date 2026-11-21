USE DB_IF0010_2026_Propi;
GO

/* =====================================================================
   OPTIMIZACIÓN CON ÍNDICES - PRUEBA DE ANTES Y DESPUÉS
   Ejecutar POR PARTES (seleccionar el bloque y F5), en orden.
   ===================================================================== */


/* =====================================================================
   PASO 0. REVISAR LOS DATOS Y LOS ÍNDICES QUE YA EXISTEN
   ===================================================================== */

-- cantidad de registros por tabla (entre más datos, más se nota el índice)
SELECT 'Clientes' AS tabla, COUNT(*) AS registros FROM proptech.Clientes
UNION ALL SELECT 'Facturas', COUNT(*) FROM proptech.Facturas
UNION ALL SELECT 'FacturasDetalle', COUNT(*) FROM proptech.FacturasDetalle
UNION ALL SELECT 'NotasCreditoDebito', COUNT(*) FROM proptech.NotasCreditoDebito
UNION ALL SELECT 'ProductosServicios', COUNT(*) FROM proptech.ProductosServicios;

-- índices que ya existen (solo deberían salir los de las llaves primarias)
SELECT t.name AS tabla, i.name AS indice, i.type_desc AS tipo
FROM sys.indexes i
INNER JOIN sys.tables t ON t.object_id = i.object_id
WHERE SCHEMA_NAME(t.schema_id) = 'proptech' AND i.name IS NOT NULL
ORDER BY t.name;

-- valores reales para usar en las consultas de prueba
SELECT TOP 1 id_cliente, COUNT(*) AS facturas
FROM proptech.Facturas GROUP BY id_cliente ORDER BY COUNT(*) DESC;

SELECT TOP 1 id_factura, COUNT(*) AS lineas
FROM proptech.FacturasDetalle GROUP BY id_factura ORDER BY COUNT(*) DESC;

SELECT TOP 1 id_producto, COUNT(*) AS veces_vendido
FROM proptech.FacturasDetalle GROUP BY id_producto ORDER BY COUNT(*) DESC;

SELECT MIN(fecha_factura) AS primera, MAX(fecha_factura) AS ultima
FROM proptech.Facturas;


/* =====================================================================
   PASO 1. BORRAR LOS ÍNDICES DE PRUEBA (por si ya se habían creado)
   Así la medición de "antes" es limpia.
   ===================================================================== */

DROP INDEX IF EXISTS IX_Facturas_IdCliente        ON proptech.Facturas;
DROP INDEX IF EXISTS IX_Facturas_Fecha            ON proptech.Facturas;
DROP INDEX IF EXISTS IX_FacturasDetalle_IdFactura ON proptech.FacturasDetalle;
DROP INDEX IF EXISTS IX_FacturasDetalle_IdProducto ON proptech.FacturasDetalle;
DROP INDEX IF EXISTS IX_Notas_IdFactura           ON proptech.NotasCreditoDebito;
GO


/* =====================================================================
   PASO 2. MEDICIÓN ANTES DE LOS ÍNDICES
   Antes de ejecutar: activar "Include Actual Execution Plan" (Ctrl + M)
   Cambiar los números (5, 10, 3 y fechas) por los del PASO 0.
   ===================================================================== */

-- limpiar caché para que la prueba sea justa
-- (si da error de permisos, se puede omitir)
CHECKPOINT;
DBCC DROPCLEANBUFFERS;
DBCC FREEPROCCACHE;
GO

SET STATISTICS IO ON;
SET STATISTICS TIME ON;
GO

-- C1: facturas de un cliente
SELECT id_factura, fecha_factura, monto_total
FROM proptech.Facturas
WHERE id_cliente = 5;

-- C2: facturas en un rango de fechas
SELECT id_factura, id_cliente, monto_total
FROM proptech.Facturas
WHERE fecha_factura BETWEEN '2026-01-01' AND '2026-03-31';

-- C3: líneas de una factura
SELECT id_producto, cantidad, monto_linea
FROM proptech.FacturasDetalle
WHERE id_factura = 10;

-- C4: ventas de un producto
SELECT id_producto, SUM(cantidad) AS unidades, SUM(monto_linea) AS total_vendido
FROM proptech.FacturasDetalle
WHERE id_producto = 3
GROUP BY id_producto;

-- C5: notas de una factura
SELECT id_nota, tipo, monto_total
FROM proptech.NotasCreditoDebito
WHERE id_factura = 10;
GO

SET STATISTICS IO OFF;
SET STATISTICS TIME OFF;
GO


/* =====================================================================
   PASO 3. SUGERENCIAS DE SQL SERVER (índices faltantes)
   Ejecutar justo después del PASO 2. Sirve como evidencia de que
   SQL Server mismo recomienda los índices.
   ===================================================================== */

SELECT d.statement AS tabla,
       d.equality_columns AS columnas_igualdad,
       d.inequality_columns AS columnas_rango,
       d.included_columns AS columnas_incluidas,
       s.avg_user_impact AS mejora_estimada_porcentaje
FROM sys.dm_db_missing_index_details d
INNER JOIN sys.dm_db_missing_index_groups g ON g.index_handle = d.index_handle
INNER JOIN sys.dm_db_missing_index_group_stats s ON s.group_handle = g.index_group_handle
WHERE d.database_id = DB_ID()
ORDER BY s.avg_user_impact DESC;


/* =====================================================================
   PASO 4. CREACIÓN DE LOS ÍNDICES
   Todos son NONCLUSTERED sobre llaves foráneas o columnas de búsqueda.
   SQL Server NO crea índices automáticos para las llaves foráneas.
   ===================================================================== */

-- búsqueda de facturas por cliente (FK id_cliente)
CREATE NONCLUSTERED INDEX IX_Facturas_IdCliente
ON proptech.Facturas (id_cliente)
INCLUDE (fecha_factura, monto_total);

-- búsqueda de facturas por rango de fechas (reportes)
CREATE NONCLUSTERED INDEX IX_Facturas_Fecha
ON proptech.Facturas (fecha_factura)
INCLUDE (id_cliente, monto_total);

-- líneas de una factura (FK id_factura)
CREATE NONCLUSTERED INDEX IX_FacturasDetalle_IdFactura
ON proptech.FacturasDetalle (id_factura)
INCLUDE (id_producto, cantidad, monto_linea);

-- ventas por producto (FK id_producto)
CREATE NONCLUSTERED INDEX IX_FacturasDetalle_IdProducto
ON proptech.FacturasDetalle (id_producto)
INCLUDE (cantidad, monto_linea);

-- notas de una factura (FK id_factura)
CREATE NONCLUSTERED INDEX IX_Notas_IdFactura
ON proptech.NotasCreditoDebito (id_factura)
INCLUDE (tipo, monto_total);
GO

-- actualizar estadísticas para que el optimizador use los índices nuevos
UPDATE STATISTICS proptech.Facturas;
UPDATE STATISTICS proptech.FacturasDetalle;
UPDATE STATISTICS proptech.NotasCreditoDebito;
GO


/* =====================================================================
   PASO 5. MEDICIÓN DESPUÉS DE LOS ÍNDICES
   Mismas consultas y mismos valores que en el PASO 2.
   ===================================================================== */

CHECKPOINT;
DBCC DROPCLEANBUFFERS;
DBCC FREEPROCCACHE;
GO

SET STATISTICS IO ON;
SET STATISTICS TIME ON;
GO

-- C1: facturas de un cliente
SELECT id_factura, fecha_factura, monto_total
FROM proptech.Facturas
WHERE id_cliente = 5;

-- C2: facturas en un rango de fechas
SELECT id_factura, id_cliente, monto_total
FROM proptech.Facturas
WHERE fecha_factura BETWEEN '2026-01-01' AND '2026-03-31';

-- C3: líneas de una factura
SELECT id_producto, cantidad, monto_linea
FROM proptech.FacturasDetalle
WHERE id_factura = 10;

-- C4: ventas de un producto
SELECT id_producto, SUM(cantidad) AS unidades, SUM(monto_linea) AS total_vendido
FROM proptech.FacturasDetalle
WHERE id_producto = 3
GROUP BY id_producto;

-- C5: notas de una factura
SELECT id_nota, tipo, monto_total
FROM proptech.NotasCreditoDebito
WHERE id_factura = 10;
GO

SET STATISTICS IO OFF;
SET STATISTICS TIME OFF;
GO


/* =====================================================================
   PASO 6. COMPROBAR QUE LOS ÍNDICES SE USARON
   La columna busquedas (seeks) debe ser mayor que 0.
   ===================================================================== */

SELECT OBJECT_NAME(s.object_id) AS tabla,
       i.name AS indice,
       s.user_seeks AS busquedas,
       s.user_scans AS recorridos,
       s.last_user_seek AS ultima_busqueda
FROM sys.dm_db_index_usage_stats s
INNER JOIN sys.indexes i ON i.object_id = s.object_id AND i.index_id = s.index_id
WHERE s.database_id = DB_ID() AND i.name LIKE 'IX_%'
ORDER BY tabla;
