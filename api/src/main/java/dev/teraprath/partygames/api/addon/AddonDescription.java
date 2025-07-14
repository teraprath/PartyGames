package dev.teraprath.partygames.api.addon;

public record AddonDescription(
    String id,
    String name,
    String version,
    String main,
    String author
) { }