-- V5 - Adicionar coluna de fotos para personagens e carros

ALTER TABLE tb_personagens
    ADD COLUMN foto VARCHAR(255);

ALTER TABLE tb_carros
ADD COLUMN foto VARCHAR(255);