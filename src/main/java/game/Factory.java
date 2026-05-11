package game;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.graphics.Color;

public class Factory {
    public static List<CarSkin> cars() {
        ArrayList<CarSkin> list = new ArrayList<CarSkin>();
        list.add(new CarSkin("starter", "Городской Мини", 0, color("1FB799"), color("EFFFFA"), 3, 1.00, "Лёгкая стартовая машина"));
        list.add(new CarSkin("taxi", "Бензин Такси", 450, color("FFD23F"), color("272D3B"), 3, 1.12, "Больше бензина за заезд"));
        list.add(new CarSkin("police", "Полиция GT", 800, color("2C7BE5"), color("FFFFFF"), 4, 1.10, "Лучше держит полосу"));
        list.add(new CarSkin("rally", "Ралли Зверь", 1200, color("FF6B35"), color("101820"), 5, 1.20, "Быстро перестраивается"));
        list.add(new CarSkin("truck", "Щит Трак", 1650, color("3D405B"), color("81B29A"), 2, 1.08, "Стартует со щитом"));
        list.add(new CarSkin("cyber", "Кибер Дрифт", 2300, color("7B2CBF"), color("80FFDB"), 5, 1.35, "Сильный бонус очков"));
        list.add(new CarSkin("fire", "Огненный Шторм", 3100, color("D00000"), color("FFD166"), 4, 1.48, "Сильный бонус очков"));
        list.add(new CarSkin("gold", "Золотая Легенда", 4500, color("D4AF37"), color("FFF8DC"), 4, 1.70, "Финальный скин"));
        return list;
    }

    public static List<Location> locations() {
        ArrayList<Location> list = new ArrayList<Location>();
        list.add(new Builder.LocationBuilder().id("metro").name("Город").price(0)
                .colors("70C7E8", "DDF4FF", "303948", "F6FAFF").objects("E55353", "FFC43D", "8CA7B8")
                .stats(1.00f, 1.00f, 1.00f).info("Спокойная городская трасса").build());
        list.add(new Builder.LocationBuilder().id("beach").name("Побережье").price(650)
                .colors("4FC3F7", "FFE9A8", "496A81", "FFF2C2").objects("F76F53", "FFE066", "55A685")
                .stats(1.05f, 1.10f, 1.18f).info("Светлая дорога у моря").build());
        list.add(new Builder.LocationBuilder().id("desert").name("Пустыня").price(1000)
                .colors("F5B461", "FFE8A3", "5A4A42", "FFF2B2").objects("B23A48", "FFE45E", "C98C4A")
                .stats(1.12f, 1.18f, 1.30f).info("Пыльно, быстро, прибыльно").build());
        list.add(new Builder.LocationBuilder().id("snow").name("Снег").price(1400)
                .colors("CDEDFD", "FFFFFF", "4B5563", "DCEBFF").objects("6C63FF", "A8F0FF", "BFD7EA")
                .stats(1.16f, 1.22f, 1.42f).info("Зимняя скользкая трасса").build());
        list.add(new Builder.LocationBuilder().id("jungle").name("Лесная трасса").price(1900)
                .colors("76C893", "E9F5DB", "2D3A3A", "B7E4C7").objects("B56576", "F4D35E", "4F772D")
                .stats(1.22f, 1.28f, 1.55f).info("Узкая зелёная трасса").build());
        list.add(new Builder.LocationBuilder().id("neon").name("Неон Ночь").price(2500)
                .colors("1B1535", "273469", "111827", "21F7C7").objects("FF4D8D", "7CF9FF", "5A189A")
                .stats(1.32f, 1.36f, 1.78f).info("Ночная сложная карта").build());
        list.add(new Builder.LocationBuilder().id("space").name("Космо Дорога").price(3400)
                .colors("050816", "372554", "15162C", "A1FCDF").objects("F72585", "F9F871", "3A0CA3")
                .stats(1.42f, 1.45f, 2.05f).info("Самая сложная локация").build());
        list.add(new Builder.LocationBuilder().id("almaty").name("Алматы Ночь").price(4200)
                .colors("0F172A", "1E293B", "242A35", "E2E8F0").objects("EF4444", "FACC15", "3F6212")
                .stats(1.30f, 1.52f, 2.15f).info("Городские огни и плотный трафик").build());
        list.add(new Builder.LocationBuilder().id("mountain").name("Горный Серпантин").price(5200)
                .colors("8ECAE6", "D8F3DC", "374151", "F8FAFC").objects("DC2626", "FDE047", "386641")
                .stats(1.24f, 1.58f, 2.30f).info("Сложные обгоны у гор").build());
        list.add(new Builder.LocationBuilder().id("rain").name("Дождливый Хайвей").price(6500)
                .colors("475569", "94A3B8", "1F2937", "CBD5E1").objects("F43F5E", "FBBF24", "334155")
                .stats(1.38f, 1.65f, 2.55f).info("Мокрый асфальт и быстрые машины").build());
        return list;
    }

    public static CarSkin car(String id) {
        List<CarSkin> list = cars();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id.equals(id)) {
                return list.get(i);
            }
        }
        return list.get(0);
    }

    public static Location location(String id) {
        List<Location> list = locations();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id.equals(id)) {
                return list.get(i);
            }
        }
        return list.get(0);
    }

    static Color color(String hex) {
        return Color.valueOf(hex);
    }
}
