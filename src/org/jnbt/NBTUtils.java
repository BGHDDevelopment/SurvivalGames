/*     */ package org.jnbt;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NBTUtils
/*     */ {
/*     */   public static String getTypeName(Class<? extends Tag> clazz)
/*     */   {
/*  49 */     if (clazz.equals(ByteArrayTag.class))
/*  50 */       return "TAG_Byte_Array";
/*  51 */     if (clazz.equals(ByteTag.class))
/*  52 */       return "TAG_Byte";
/*  53 */     if (clazz.equals(CompoundTag.class))
/*  54 */       return "TAG_Compound";
/*  55 */     if (clazz.equals(DoubleTag.class))
/*  56 */       return "TAG_Double";
/*  57 */     if (clazz.equals(EndTag.class))
/*  58 */       return "TAG_End";
/*  59 */     if (clazz.equals(FloatTag.class))
/*  60 */       return "TAG_Float";
/*  61 */     if (clazz.equals(IntTag.class))
/*  62 */       return "TAG_Int";
/*  63 */     if (clazz.equals(ListTag.class))
/*  64 */       return "TAG_List";
/*  65 */     if (clazz.equals(LongTag.class))
/*  66 */       return "TAG_Long";
/*  67 */     if (clazz.equals(ShortTag.class))
/*  68 */       return "TAG_Short";
/*  69 */     if (clazz.equals(StringTag.class)) {
/*  70 */       return "TAG_String";
/*     */     }
/*  72 */     throw new IllegalArgumentException("Invalid tag classs (" + clazz.getName() + ").");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getTypeCode(Class<? extends Tag> clazz)
/*     */   {
/*  83 */     if (clazz.equals(ByteArrayTag.class))
/*  84 */       return 7;
/*  85 */     if (clazz.equals(ByteTag.class))
/*  86 */       return 1;
/*  87 */     if (clazz.equals(CompoundTag.class))
/*  88 */       return 10;
/*  89 */     if (clazz.equals(DoubleTag.class))
/*  90 */       return 6;
/*  91 */     if (clazz.equals(EndTag.class))
/*  92 */       return 0;
/*  93 */     if (clazz.equals(FloatTag.class))
/*  94 */       return 5;
/*  95 */     if (clazz.equals(IntTag.class))
/*  96 */       return 3;
/*  97 */     if (clazz.equals(ListTag.class))
/*  98 */       return 9;
/*  99 */     if (clazz.equals(LongTag.class))
/* 100 */       return 4;
/* 101 */     if (clazz.equals(ShortTag.class))
/* 102 */       return 2;
/* 103 */     if (clazz.equals(StringTag.class)) {
/* 104 */       return 8;
/*     */     }
/* 106 */     throw new IllegalArgumentException("Invalid tag classs (" + clazz.getName() + ").");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Class<? extends Tag> getTypeClass(int type)
/*     */   {
/* 117 */     switch (type) {
/*     */     case 0: 
/* 119 */       return EndTag.class;
/*     */     case 1: 
/* 121 */       return ByteTag.class;
/*     */     case 2: 
/* 123 */       return ShortTag.class;
/*     */     case 3: 
/* 125 */       return IntTag.class;
/*     */     case 4: 
/* 127 */       return LongTag.class;
/*     */     case 5: 
/* 129 */       return FloatTag.class;
/*     */     case 6: 
/* 131 */       return DoubleTag.class;
/*     */     case 7: 
/* 133 */       return ByteArrayTag.class;
/*     */     case 8: 
/* 135 */       return StringTag.class;
/*     */     case 9: 
/* 137 */       return ListTag.class;
/*     */     case 10: 
/* 139 */       return CompoundTag.class;
/*     */     }
/* 141 */     throw new IllegalArgumentException("Invalid tag type : " + type + ".");
/*     */   }
/*     */ }


/* Location:              C:\Users\JJ\Downloads\Minecraft\Plugins\Dungeons.jar!\org\jnbt\NBTUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */