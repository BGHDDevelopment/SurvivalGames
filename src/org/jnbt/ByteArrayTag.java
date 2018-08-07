/*    */ package org.jnbt;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ByteArrayTag
/*    */   extends Tag
/*    */ {
/*    */   private final byte[] value;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ByteArrayTag(String name, byte[] value)
/*    */   {
/* 54 */     super(name);
/* 55 */     this.value = value;
/*    */   }
/*    */   
/*    */   public byte[] getValue()
/*    */   {
/* 60 */     return this.value;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 65 */     StringBuilder hex = new StringBuilder();
/* 66 */     byte[] arrayOfByte; int j = (arrayOfByte = this.value).length; for (int i = 0; i < j; i++) { byte b = arrayOfByte[i];
/* 67 */       String hexDigits = Integer.toHexString(b).toUpperCase();
/* 68 */       if (hexDigits.length() == 1) {
/* 69 */         hex.append("0");
/*    */       }
/* 71 */       hex.append(hexDigits).append(" ");
/*    */     }
/* 73 */     String name = getName();
/* 74 */     String append = "";
/* 75 */     if ((name != null) && (!name.equals(""))) {
/* 76 */       append = "(\"" + getName() + "\")";
/*    */     }
/* 78 */     return "TAG_Byte_Array" + append + ": " + hex.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\JJ\Downloads\Minecraft\Plugins\Dungeons.jar!\org\jnbt\ByteArrayTag.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */