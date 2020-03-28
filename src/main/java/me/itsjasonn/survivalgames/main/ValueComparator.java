package me.itsjasonn.survivalgames.main;

import java.util.Comparator;
import java.util.Map;
import org.bukkit.entity.Player;

class ValueComparator
  implements Comparator<Player>
{
  Map<Player, Integer> base;
  
  public ValueComparator(Map<Player, Integer> map)
  {
    this.base = map;
  }
  
  public int compare(Player a, Player b)
  {
    if (((Integer)this.base.get(a)).intValue() >= ((Integer)this.base.get(b)).intValue()) {
      return -1;
    }
    return 1;
  }
}
