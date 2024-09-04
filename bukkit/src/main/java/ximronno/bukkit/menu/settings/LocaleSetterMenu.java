package ximronno.bukkit.menu.settings;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ximronno.bukkit.menu.DiorePaginatedDataMenu;
import ximronno.bukkit.menu.yap.AccountMenu;
import ximronno.bukkit.message.type.LanguagePath;
import ximronno.bukkit.message.type.menu.MenuItemAdditionsPaths;
import ximronno.bukkit.message.type.menu.MenuNamesPaths;
import ximronno.diore.api.DiorePlugin;
import ximronno.diore.api.account.Account;
import ximronno.diore.api.item.ItemBuilder;
import ximronno.diore.api.menu.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LocaleSetterMenu extends DiorePaginatedDataMenu {

    private static final NamespacedKey key = DiorePlugin.getKey();

    @Override
    public int getMaxItemsPerPage() {
        return 28;
    }

    @Override
    public String getTitle(Locale locale) {
        return api.getMessageManager().getMessage(MenuNamesPaths.LOCALE_SETTER_MENU_NAME, locale, true);
    }

    @Override
    public Menu getPreviousMenu() {
        return new AccountMenu();
    }

    @Override
    public void setContents(Player p, Account acc, Locale locale) {
        addBorder(locale, false);

        List<Locale> providedLocales = messageManager.getMessageProvider().getProvidedLanguages().stream().toList();
        int iterator = 10;
        if(!providedLocales.isEmpty()) {
            for(int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * currentPage + i;
                if(index >= providedLocales.size()) {
                    break;
                }
                if(providedLocales.get(index) != null) {
                    Locale targetLocale = providedLocales.get(index);

                    boolean equippedLocale = targetLocale.equals(locale);

                    inventory.setItem(iterator, ItemBuilder.builder()
                            .setMaterial(Material.PLAYER_HEAD)
                            .setProfileFromURL(messageManager.getTexturesURL(targetLocale))
                            .setDisplayName(messageManager.getMessage(LanguagePath.NAME, targetLocale, true))
                            .setLore(getLanguageItemList(locale, targetLocale, equippedLocale))
                            .addPersistentData(key, targetLocale.toString())
                            .build());

                    if((iterator + 2) % 9 == 0) iterator += 2;
                    iterator++;
                }
            }
        }
        hasNextPage = !((index + 1) >= providedLocales.size());
        addButtons(locale, true);
    }

    private List<String> getLanguageItemList(Locale locale, Locale targetLocale, boolean equippedLocale) {
        List<String> list = new ArrayList<>();

        list.add(messageManager.getMessage(MenuItemAdditionsPaths.LANGUAGE_FORMAT, locale, true, Map.of("{raw_format}", targetLocale.toString())));


        if(equippedLocale) {
            list.add(messageManager.getMessage(MenuItemAdditionsPaths.LANGUAGE, locale, true));
        }
        if(api.getMessageManager().getMessageProvider().getDefaultLanguage().equals(targetLocale)) {

            list.add(messageManager.getMessage(MenuItemAdditionsPaths.LANGUAGE_DEFAULT, locale, true));

        }

        return list;
    }

    @Override
    public void handleMenu(Player p, Account acc, Locale locale, InventoryClickEvent e, int slot, PersistentDataContainer container) {
        if(container.has(key, PersistentDataType.STRING)) {
            String containerString = container.get(key, PersistentDataType.STRING);
            if(containerString == null) return;

            Locale targetLocale = new Locale(containerString);
            api.getAccountController().setLocale(p, acc, targetLocale);

            openPreviousMenu(p);
        }

     }
}
