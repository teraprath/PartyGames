package dev.teraprath.partygames.api.module;

public class ModuleDescription {

    private final String name;
    private final String version;
    private final String mainClass;
    private final String author;
    private final String description;
    private final String website;

    public ModuleDescription(String name, String version, String mainClass, String author, String description, String website) {
        this.name = name;
        this.version = version;
        this.mainClass = mainClass;
        this.author = author;
        this.description = description;
        this.website = website;
    }

    public String getName() { return name; }
    public String getVersion() { return version; }
    public String getMainClass() { return mainClass; }
    public String getAuthor() { return author; }
    public String getDescription() { return description; }
    public String getWebsite() { return website; }
}