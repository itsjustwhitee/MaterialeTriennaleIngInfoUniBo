package beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Cart implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Map<Item, Integer> items;
	private int group;
	
	public Cart() {
		super();
		items = new HashMap<>();
		group = -1;
	}
	
	public Cart(int group) {
		super();
		items = new HashMap<>();
		this.group = group;
	}

	public Map<Item, Integer> getItems() {
		return items;
	}

	public void setItems(Map<Item, Integer> items) {
		this.items = items;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public void add(Item i)
	{
		for(Entry<Item, Integer> e : items.entrySet())
		{
			if(e.getKey().getId().equals(i.getId()))
			{
				items.replace(e.getKey(), e.getValue() + 1);
				return;
			}
		}
		
		items.put(i, 1);
	}
	
	public void remove(Item i)
	{
		for(Entry<Item, Integer> e : items.entrySet())
		{
			if(e.getKey().getId().equals(i.getId()))
			{
				items.replace(e.getKey(), e.getValue() - 1);
				if(items.get(e.getKey()) < 1)
					items.remove(e.getKey());
				return;
			}
		}
	}
	
	public int getQuantity(String id)
	{
		for(Entry<Item, Integer> e : items.entrySet())
		{
			if(e.getKey().getId().equals(id))
			{
				return e.getValue();
			}
		}
		return 0;
	}
	
	public double getTotal()
	{
		Double total = 0.0;
		for(Entry<Item, Integer> e : items.entrySet())
		{
			total += (e.getValue() * e.getKey().getPrice());
		}
		return total;
	}
	
	public boolean isEmpty()
	{
		return items.isEmpty();
	}
	
	@Override
	public String toString()
	{
		String s = "Group " + this.group + "\n";
		for(Entry<Item, Integer> e : items.entrySet())
		{
			s += "\t>" + e.getKey().toString() + " x" + e.getValue() + "\n\t" + e.getKey().getDescription() + "\n\n";
		}
		
		return s;
	}
}
