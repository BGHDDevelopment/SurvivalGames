/*     */ package org.jnbt;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.zip.GZIPInputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NBTInputStream
/*     */   implements Closeable
/*     */ {
/*     */   private final DataInputStream is;
/*     */   
			private int length;
			
/*     */   public NBTInputStream(InputStream is)
/*     */     throws IOException
/*     */   {
/*  71 */     this.is = new DataInputStream(new GZIPInputStream(is));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Tag readTag()
/*     */     throws IOException
/*     */   {
/*  80 */     return readTag(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Tag readTag(int depth)
/*     */     throws IOException
/*     */   {
/*  90 */     int type = this.is.readByte() & 0xFF;
/*     */     String name;
/*  93 */     if (type != 0) {
/*  94 */       int nameLength = this.is.readShort() & 0xFFFF;
/*  95 */       byte[] nameBytes = new byte[nameLength];
/*  96 */       this.is.readFully(nameBytes);
/*  97 */       name = new String(nameBytes, NBTConstants.CHARSET);
/*     */     } else {
/*  99 */       name = "";
/*     */     }
/*     */     
/* 102 */     return readTagPayload(type, name, depth);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Tag readTagPayload(int type, String name, int depth)
/*     */     throws IOException
/*     */   {
/* 114 */     switch (type) {
/*     */     case 0: 
/* 116 */       if (depth == 0) {
/* 117 */         throw new IOException("TAG_End found without a TAG_Compound/TAG_List tag preceding it.");
/*     */       }
/* 119 */       return new EndTag();
/*     */     
/*     */     case 1: 
/* 122 */       return new ByteTag(name, this.is.readByte());
/*     */     case 2: 
/* 124 */       return new ShortTag(name, this.is.readShort());
/*     */     case 3: 
/* 126 */       return new IntTag(name, this.is.readInt());
/*     */     case 4: 
/* 128 */       return new LongTag(name, this.is.readLong());
/*     */     case 5: 
/* 130 */       return new FloatTag(name, this.is.readFloat());
/*     */     case 6: 
/* 132 */       return new DoubleTag(name, this.is.readDouble());
/*     */     case 7: 
/* 134 */       length = this.is.readInt();
/* 135 */       byte[] bytes = new byte[length];
/* 136 */       this.is.readFully(bytes);
/* 137 */       return new ByteArrayTag(name, bytes);
/*     */     case 8: 
/* 139 */       length = this.is.readShort();
/* 140 */       byte[] bytes1 = new byte[length];
/* 141 */       this.is.readFully(bytes1);
/* 142 */       return new StringTag(name, new String(bytes1, NBTConstants.CHARSET));
/*     */     case 9: 
/* 144 */       int childType = this.is.readByte();
/*     */       
/* 147 */       List<Tag> tagList = new ArrayList<Tag>();
/* 148 */       for (int i = 0; i < length; i++) {
/* 149 */         Tag tag = readTagPayload(childType, "", depth + 1);
/* 150 */         if ((tag instanceof EndTag)) {
/* 151 */           throw new IOException("TAG_End not permitted in a list.");
/*     */         }
/* 153 */         tagList.add(tag);
/*     */       }
/*     */       
/* 156 */       return new ListTag(name, NBTUtils.getTypeClass(childType), tagList);
/*     */     case 10: 
/* 158 */       Map<String, Tag> tagMap = new HashMap<String, Tag>();
/*     */       for (;;) {
/* 160 */         Tag tag = readTag(depth + 1);
/* 161 */         if ((tag instanceof EndTag)) {
/*     */           break;
/*     */         }
/* 164 */         tagMap.put(tag.getName(), tag);
/*     */       }
/*     */       
/*     */ 
/* 168 */       return new CompoundTag(name, tagMap);
/*     */     }
/* 170 */     throw new IOException("Invalid tag type: " + type + ".");
/*     */   }
/*     */   
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 176 */     this.is.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\JJ\Downloads\Minecraft\Plugins\Dungeons.jar!\org\jnbt\NBTInputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */