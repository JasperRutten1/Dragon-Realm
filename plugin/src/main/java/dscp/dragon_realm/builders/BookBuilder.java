package dscp.dragon_realm.builders;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BookBuilder {
    private BookMeta meta;

    public BookBuilder(){
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        if(meta instanceof BookMeta) this.meta = (BookMeta) meta;
    }

    public BookBuilder setTitle(String title) {
        if(title == null) throw new IllegalArgumentException("title can't be null");
        this.meta.setTitle(title);
        return this;
    }

    public BookBuilder setAuthor(String author){
        if(author == null) throw new IllegalArgumentException("author can't be null");
        this.meta.setAuthor(author);
        return this;
    }

    public BookBuilder setLore(List<String> lore){
        if(lore == null) throw new IllegalArgumentException("lore can't be null");
        this.meta.setLore(lore);
        return this;
    }

    public BookBuilder setLore(LoreBuilder lore){
        setLore(lore.build());
        return this;
    }

    public BookBuilder setLore(String string){
        List<String> lore = new ArrayList<>();
        lore.add(string);
        setLore(lore);
        return this;
    }

    public BookBuilder addPage(String page){
        if(page == null){
            meta.addPage("");
        }
        else{
            meta.addPage(page);
        }
        return this;
    }

    public BookBuilder addPage(BookPageBuilder page){
        if(page == null){
            addPage("");
        }
        else{
            addPage(page.build());
        }
        return this;
    }

    public ItemStack getBook(int amount){
        ItemStack book = new ItemStack(Material.BOOK, amount);
        book.setItemMeta(this.meta);
        return book;
    }

    public ItemStack getBook(){
        return getBook(1);
    }

    public class BookPageBuilder {
        private ArrayList<String> lines;

        public BookPageBuilder(){
            this.lines = new ArrayList<>();
        }

        public BookPageBuilder addLine(String line){
            if(line == null){
                lines.add("");
            }
            else{
                lines.add(line);
            }
            return this;
        }

        public BookPageBuilder addLineIf(String line, boolean condition){
            if(condition){
                addLine(line);
            }
            return this;
        }

        private String build(){
            StringBuilder sb = new StringBuilder();
            for(String line : lines){
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }
}
