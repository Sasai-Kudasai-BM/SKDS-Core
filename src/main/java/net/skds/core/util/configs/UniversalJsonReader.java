/*     */ package net.skds.core.util.configs;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.stream.JsonReader;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import net.minecraft.resources.DataPackRegistries;
/*     */ import net.skds.core.SKDSCore;
/*     */ import net.skds.core.api.IJsonConfigUnit;
/*     */ 
/*     */ 
/*     */ public class UniversalJsonReader
/*     */ {
/*  25 */   public static DataPackRegistries DATA_PACK_RREGISTRIES = null;
/*     */   
/*     */   public DataPackRegistries getDPR() {
/*  28 */     return DATA_PACK_RREGISTRIES;
/*     */   }
/*     */   
/*     */   public static boolean read(IJsonConfigUnit unit) {
/*  32 */     File configDir = new File(unit.getPath());
/*     */     try {
/*  34 */       configDir.mkdirs();
/*     */       
/*  36 */       File readFile = new File(configDir, unit.getFormatedName());
/*  37 */       boolean exsists = readFile.exists();
/*  38 */       if (!exsists) {
/*  39 */         create(unit);
/*     */       }
/*     */       
/*  42 */       if (!interpret(unit, readFile)) {
/*  43 */         if (!exsists) {
/*  44 */           return false;
/*     */         }
/*  46 */         create(unit);
/*  47 */         if (!interpret(unit, readFile)) {
/*  48 */           return false;
/*     */         }
/*     */       }
/*     */     
/*  52 */     } catch (Exception e) {
/*  53 */       return false;
/*     */     } 
/*  55 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean readResource(IJsonConfigUnit unit, InputStream is) {
/*     */     try {
/*  61 */       if (!interpretResource(unit, is)) {
/*  62 */         return false;
/*     */       }
/*     */     }
/*  65 */     catch (Exception e) {
/*  66 */       return false;
/*     */     } 
/*  68 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean interpret(IJsonConfigUnit unit, File reading) throws IOException {
/*  72 */     JsonObject jsonobject = new JsonObject();
/*     */     
/*  74 */     InputStream inputStream = new FileInputStream(reading);
/*  75 */     Reader r = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
/*  76 */     JsonReader jsonReader = new JsonReader(r);
/*  77 */     Gson GSON = new Gson();
/*     */     try {
/*  79 */       jsonobject = (JsonObject)GSON.getAdapter(JsonObject.class).read(jsonReader);
/*  80 */     } catch (IOException e) {
/*  81 */       SKDSCore.LOGGER.error("Empty or invalid config file! " + unit.getFormatedName());
/*  82 */       inputStream.close();
/*     */       
/*  84 */       create(unit);
/*  85 */       inputStream = new FileInputStream(reading);
/*  86 */       r = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
/*  87 */       jsonReader = new JsonReader(r);
/*     */       
/*  89 */       jsonobject = (JsonObject)GSON.getAdapter(JsonObject.class).read(jsonReader);
/*     */     } 
/*  91 */     r.close();
/*  92 */     jsonReader.close();
/*     */     
/*  94 */     return unit.apply(jsonobject);
/*     */   }
/*     */   
/*     */   private static boolean interpretResource(IJsonConfigUnit unit, InputStream inputStream) throws IOException {
/*  98 */     JsonObject jsonobject = new JsonObject();
/*     */     
/* 100 */     Reader r = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
/* 101 */     JsonReader jsonReader = new JsonReader(r);
/* 102 */     Gson GSON = new Gson();
/*     */     try {
/* 104 */       jsonobject = (JsonObject)GSON.getAdapter(JsonObject.class).read(jsonReader);
/* 105 */     } catch (IOException e) {
/* 106 */       SKDSCore.LOGGER.error("Empty or invalid config data file! " + unit.getFormatedName());
/* 107 */       r.close();
/* 108 */       jsonReader.close();
/* 109 */       return false;
/*     */     } 
/* 111 */     r.close();
/* 112 */     jsonReader.close();
/*     */     
/* 114 */     return unit.apply(jsonobject);
/*     */   }
/*     */   
/*     */   private static boolean create(IJsonConfigUnit unit) throws IOException {
/* 118 */     BufferedInputStream is = new BufferedInputStream(unit.getClass().getClassLoader().getResourceAsStream(unit.getJarPath() + "\\" + unit.getFormatedName()));
/*     */     
/* 120 */     File configFile = new File(unit.getPath(), unit.getFormatedName());
/* 121 */     if (configFile.exists()) {
/* 122 */       backup(unit);
/*     */     }
/* 124 */     Files.copy(is, configFile.toPath(), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/* 125 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean backup(IJsonConfigUnit unit) throws IOException {
/* 129 */     File configFile = new File(unit.getPath(), unit.getFormatedName());
/* 130 */     File backupFile = new File(unit.getPath(), unit.getName() + "-backup." + unit.getFormat());
/* 131 */     int index = 0;
/* 132 */     while (backupFile.exists()) {
/* 133 */       index++;
/* 134 */       backupFile = new File(unit.getPath(), unit.getName() + "-backup (" + index + ") ." + unit.getFormat());
/*     */     } 
/* 136 */     SKDSCore.LOGGER.error("Creating config backup" + unit.getFormatedName());
/* 137 */     Files.copy(configFile.toPath(), backupFile.toPath(), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/* 138 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Games\Minecraft\forge\BPO\libs\skds_core-1.16.5-0.2.0-deobf.jar!\net\skds\cor\\util\configs\UniversalJsonReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */