/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package showitem;

import static java.lang.System.out;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Administrator
 */
public class ShowItem extends JavaPlugin{

    String cmd;
    public class 物品展示代码 {
        public String 创建字符串(String 输入){
            String 字符串="{text:\""+输入+"\"},";
            return 字符串;
        }
         public String 创建指令(ItemStack 物品){
             //Map<Enchantment,Integer> 附魔列表=物品属性.getEnchants();
             String 字符串;
             if(物品.hasItemMeta()){
             ItemMeta 物品属性=物品.getItemMeta();
             List<String> 物品附加值=物品属性.getLore();
             String 物品名称=物品属性.getDisplayName();
             String 物品附加值名称;
             String 物品附魔字符串;
             if(物品属性.hasLore()){
                 物品附加值名称="[";
                 for(String 值:物品附加值){
                     物品附加值名称+="\\\"";
                     物品附加值名称+=值;
                     物品附加值名称+="\\\",";
                 }
                 物品附加值名称+="],";
             }else{
                 物品附加值名称="[],";
             }
             if(物品属性.hasEnchants()){
                 Map<Enchantment,Integer> 附魔列表=物品属性.getEnchants();
                 物品附魔字符串="[";
                 for(Map.Entry<Enchantment,Integer> 附魔值:附魔列表.entrySet()){
                     Enchantment 附魔id=(Enchantment)附魔值.getKey();
                     int 附魔等级=(int)附魔值.getValue();
                     物品附魔字符串+="{id:"+附魔id.getId()+",lvl:"+附魔等级+"},";
                 }
                 物品附魔字符串+="],";
             }else{
                 物品附魔字符串="[],";
             }
             字符串="{text:\"["+物品名称+"]\",hoverEvent:{action:show_item,value:\"{id:"+物品.getTypeId()+",tag:{display:{Lore:"+物品附加值名称+"},ench:"+物品附魔字符串+"}}\"}},";
             }
             else{
                 字符串="{text:\"["+物品.getType().toString()+"]\",hoverEvent:{action:show_item,value:\"{id:"+物品.getTypeId()+"}\"}},";
             }
             return 字符串;
         }
    }
    public class 事件监听器 implements Listener{
        @EventHandler
        public void 玩家聊天事件(AsyncPlayerChatEvent 事件){
            Player 玩家=事件.getPlayer();
            String 信息=事件.getMessage();
            String 输出信息=信息;
            String 指定字符串;
            int itemflag=0;
            int lastpos=0;
            物品展示代码 代码=new 物品展示代码();
            for(int i=0;i<9;i++){
                指定字符串="["+i+"]";
                int pos=信息.indexOf(指定字符串);
                if(pos!=-1){
                    if(itemflag==0){
                        输出信息=" {text:\"<"+玩家.getName()+"> \",extra:[";                        
                    }
                    itemflag++;
                    ItemStack 物品=玩家.getInventory().getItem(i);
                    if(物品.getType()==Material.AIR){
                            continue;
                    }
                    输出信息+=代码.创建字符串(信息.substring(lastpos, pos));
                    输出信息+=代码.创建指令(物品);
                    lastpos=pos+3;
                }
            }
            if(itemflag!=0){
                if(lastpos!=信息.length())
                    输出信息+=代码.创建字符串(信息.substring(lastpos, 信息.length()));
                输出信息+="{text:\"\"}]}";
                out.print("tellraw KeyMove"+输出信息);
                for(Player 接收玩家:getServer().getOnlinePlayers()){
                    Bukkit.dispatchCommand(getServer().getConsoleSender(), "tellraw "+接收玩家.getName()+输出信息);
                }
                事件.setCancelled(true);
            }
        }
    }

    
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new 事件监听器(),this);
        out.print("物品展示已加载!");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
