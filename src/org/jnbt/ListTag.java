/*    */ package org.jnbt;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ListTag
/*    */   extends Tag
/*    */ {
/*    */   private final Class<? extends Tag> type;
/*    */   private final List<Tag> value;
/*    */   
/*    */   public ListTag(String name, Class<? extends Tag> type, List<Tag> value)
/*    */   {
/* 63 */     super(name);
/* 64 */     this.type = type;
/* 65 */     this.value = Collections.unmodifiableList(value);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Class<? extends Tag> getType()
/*    */   {
/* 73 */     return this.type;
/*    */   }
/*    */   
/*    */   public List<Tag> getValue()
/*    */   {
/* 78 */     return this.value;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 83 */     String name = getName();
/* 84 */     String append = "";
/* 85 */     if ((name != null) && (!name.equals(""))) {
/* 86 */       append = "(\"" + getName() + "\")";
/*    */     }
/* 88 */     StringBuilder bldr = new StringBuilder();
/* 89 */     bldr.append("TAG_List" + append + ": " + this.value.size() + " entries of type " + NBTUtils.getTypeName(this.type) + "\r\n{\r\n");
/* 90 */     for (Tag t : this.value) {
/* 91 */       bldr.append("   " + t.toString().replaceAll("\r\n", "\r\n   ") + "\r\n");
/*    */     }
/* 93 */     bldr.append("}");
/* 94 */     return bldr.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\JJ\Downloads\Minecraft\Plugins\Dungeons.jar!\org\jnbt\ListTag.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */