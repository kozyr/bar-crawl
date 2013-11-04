# --- !Ups
ALTER chicago_walking_2po_4pgr ADD geom GEOMETRY;
UPDATE chicago_walking_2po_4pgr set geom=ST_Transform(geog, 3857);
CALL CreateSpatialIndex(null, 'SPATIAL', 'GEOM', '3857');