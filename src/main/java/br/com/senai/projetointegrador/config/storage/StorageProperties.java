package br.com.senai.projetointegrador.config.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

@ConfigurationProperties(prefix = "app.storage")
public record StorageProperties(Path studentPictures) {}
