package com.br.fasipe.menubase.biomedicina.dto;

public class ModuleThemeDTO {
    private String sistema;
    private String logoBase64;
    private String colorPrimary;
    private String colorSecondary; // Usado para botões e detalhes
    private String colorText;      // Usado para textos gerais
    private String fontFamily;
    private String fontSize;
    
    // --- NOVO CAMPO ---
    private String language; // "pt", "en", "es"

    public ModuleThemeDTO() {}

    // Getters e Setters
    public String getSistema() { return sistema; }
    public void setSistema(String sistema) { this.sistema = sistema; }
    public String getLogoBase64() { return logoBase64; }
    public void setLogoBase64(String logoBase64) { this.logoBase64 = logoBase64; }
    public String getColorPrimary() { return colorPrimary; }
    public void setColorPrimary(String colorPrimary) { this.colorPrimary = colorPrimary; }
    public String getColorSecondary() { return colorSecondary; }
    public void setColorSecondary(String colorSecondary) { this.colorSecondary = colorSecondary; }
    public String getColorText() { return colorText; }
    public void setColorText(String colorText) { this.colorText = colorText; }
    public String getFontFamily() { return fontFamily; }
    public void setFontFamily(String fontFamily) { this.fontFamily = fontFamily; }
    public String getFontSize() { return fontSize; }
    public void setFontSize(String fontSize) { this.fontSize = fontSize; }
    
    // Novo Getter/Setter
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}