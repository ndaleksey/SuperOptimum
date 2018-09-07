package com.denlex.superoptimum;

import com.denlex.superoptimum.domain.location.Address;
import com.denlex.superoptimum.domain.location.City;
import com.denlex.superoptimum.domain.location.Country;
import com.denlex.superoptimum.domain.location.Region;
import com.denlex.superoptimum.domain.product.*;
import com.denlex.superoptimum.domain.user.*;
import com.denlex.superoptimum.service.location.AddressService;
import com.denlex.superoptimum.service.location.CityService;
import com.denlex.superoptimum.service.location.CountryService;
import com.denlex.superoptimum.service.product.CategoryService;
import com.denlex.superoptimum.service.product.ProductService;
import com.denlex.superoptimum.service.user.ContactService;
import com.denlex.superoptimum.service.user.CustomerService;
import com.denlex.superoptimum.service.user.DistributorService;
import com.denlex.superoptimum.service.user.RoleService;
import com.denlex.superoptimum.service.user.impl.CustomerCredentialsService;
import com.denlex.superoptimum.service.user.impl.DistributorCredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Shishkov A.V. on 07.08.18.
 */
@Component
public class AppStartupRunner implements ApplicationRunner {
	@Autowired
	private RoleService roleService;

	@Autowired
	@Qualifier(value = "customerCredentialsService")
	private CustomerCredentialsService customerCredentialsService;

	@Autowired
	@Qualifier(value = "distributorCredentialsService")
	private DistributorCredentialsService distributorCredentialsService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private DistributorService distributorService;

	@Autowired
	private CountryService countryService;

	@Autowired
	private CityService cityService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ContactService contactService;

	@Autowired
	private ProductService productService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
//		initAll();
	}

	private void initAll() {
		fillLocations();
		fillProducts();
		fillRoles();
		fillCustomers();
		fillDistributors();
	}

	private void fillLocations() {
		Country russia = new Country("Россия");

		Region kurskRegion = new Region("Курская обл.", 46, russia);
		City kursk = new City("Курск", kurskRegion);
		City jelenzogorsk = new City("Железногорск", kurskRegion);
		kurskRegion.addCities(kursk, jelenzogorsk);

		Region belgorodRegion = new Region("Белгородская обл.", 31, russia);
		City belgorod = new City("Белгород", belgorodRegion);
		City stOskol = new City("Ст.Оскол", belgorodRegion);
		belgorodRegion.addCities(belgorod, stOskol);

		Region voronejRegion = new Region("Воронежская обл.", 36, russia);
		City voronej = new City("Воронеж", voronejRegion);
		voronejRegion.addCity(voronej);

		russia.addRegions(kurskRegion, belgorodRegion, voronejRegion);

		countryService.save(russia);

		Country usa = new Country("США");
		Country ukraine = new Country("Украина");

		countryService.save(usa);
		countryService.save(ukraine);
	}

	private void fillProducts() {
		Category cosmetology = new Category("Косметика");

		Subcategory injection = new Subcategory("Инъекционная");
		injection.addProducts(
				new Product(injection, "Крем для бритья", null, null, null, LocalDate.now()),
				new Product(injection, "Ласьон для лица", null, null, null, LocalDate.now()));
		cosmetology.addSubcategory(injection);

		Subcategory app = new Subcategory("Аппаратная");
		app.addProduct(
				new Product(app, "Инструменты для косметики", "Otesaly", "good", null, LocalDate.now()));
		app.addProduct(
				new Product(app, "Cutting tools", null, null, null, LocalDate.now()));
		cosmetology.addSubcategory(app);

		Subcategory hair = new Subcategory("Уход за волосами");

		hair.addProduct(
				new Product(hair, "Крем для волос", null, null, null, LocalDate.now()));
		cosmetology.addSubcategory(hair);

		categoryService.save(cosmetology);

		Category depilation = new Category("Депиляция");

		Subcategory dep = new Subcategory("Депиряций");
		dep.addProduct(
				new Product(dep, "Skin removal", "flash", null, null, LocalDate.now()));
		dep.addProduct(
				new Product(dep, "Cosco things", null, null, null, LocalDate.now()));

		depilation.addSubcategory(dep);
		categoryService.save(depilation);

		Category solar = new Category("Солярий");
		solar.addSubcategory(new Subcategory("Солярий"));
		categoryService.save(solar);

		Category massage = new Category("Массаж");
		massage.addSubcategory(new Subcategory("Массаж"));
		categoryService.save(massage);

		Category barbershop = new Category("Парикмахерская продукция");
		barbershop.addSubcategories(
				new Subcategory("Стайлинг"),
				new Subcategory("Наращивание волос"),
				new Subcategory("Инструменты, аксесуары и расходные материалы"));
		categoryService.save(barbershop);

		Category nails = new Category("Ногтевой сервис");
		nails.addSubcategories(
				new Subcategory("Моделирование"),
				new Subcategory("Уход за ногтями и кожей рук"),
				new Subcategory("Декор ногтей"),
				new Subcategory("Инструменты и техника"),
				new Subcategory("Расходные материалы"));
		categoryService.save(nails);

		Category eyelashbrow = new Category("Ресницы и брови");
		eyelashbrow.addSubcategories(
				new Subcategory("Оформление бровей"),
				new Subcategory("Наращивание ресниц"),
				new Subcategory("Ламинирование ресниц"),
				new Subcategory("Микропигментирование"),
				new Subcategory("Инструменты и расходные материалы"));
		categoryService.save(eyelashbrow);

		Category makeup = new Category("Визаж");
		makeup.addSubcategories(
				new Subcategory("Макияж лица"),
				new Subcategory("Макияж глаз"),
				new Subcategory("Макияж губ"),
				new Subcategory("Кисти для Макияжа"),
				new Subcategory("Кейсы и палитра"),
				new Subcategory("Инструменты"));
		categoryService.save(makeup);

		Category tattooAndPiercing = new Category("Татуаж и пирсинг");
		tattooAndPiercing.addSubcategories(
				new Subcategory("Татуаж"),
				new Subcategory("Пирсинг"));
		categoryService.save(tattooAndPiercing);

		Category staff = new Category("Расходные материалы и одноразовые принадлежности");
		Subcategory cleaner = new Subcategory("Одноразовые простыни,полотенца, салфетки");
		cleaner.addProducts(
				new Product(cleaner, "Полотенца", null, null, null, LocalDate.now()),
				new Product(cleaner, "Простыни", null, null, null, LocalDate.now()),
				new Product(cleaner, "Салфетки", null, null, null, LocalDate.now()),
				new Product(cleaner, "Tissues", null, null, null, LocalDate.now()));

		staff.addSubcategories(
				cleaner,
				new Subcategory("Одноразовая одежда и перчатки"),
				new Subcategory("Кисти, шпатели, баночки"),
				new Subcategory("Ватные диски, спонжи"),
				new Subcategory("Другое"));
		categoryService.save(staff);

		Category sterilization = new Category("Стерилизация и дезинфекция");
		sterilization.addSubcategories(
				new Subcategory("Средства для обработки кожи"),
				new Subcategory("Средства для обработки инструментов и поверхностей"),
				new Subcategory("Стерилизаторы, принадлежности и расходники"));
		categoryService.save(sterilization);

		Category electronics = new Category("Электроника");

		Subcategory smartphones = new Subcategory("Смартфоны", electronics);
		smartphones.addProducts(
				new Product(smartphones, "iPhone 8", null, null, "Apple Co", LocalDate.now()),
				new Product(smartphones, "iPhone 10", null, null, "Apple Co", LocalDate.now()),
				new Product(smartphones, "Samsung A8", null, null, "Samsung", LocalDate.now()));

		Subcategory tablets = new Subcategory("Планшеты", electronics);
		Subcategory notebooks = new Subcategory("Ноутбуки", electronics);
		notebooks.addProducts(
				new Product(tablets, "Acer Aspire", null, null, null, LocalDate.now()),
				new Product(tablets, "MacBook Pro", null, null, "Apple Co", LocalDate.now()));
				Subcategory smartTV = new Subcategory("Смарт ТВ", electronics);
		electronics.addSubcategories(smartphones, tablets, notebooks, smartTV);
		categoryService.save(electronics);
	}

	private void fillRoles() {
		Role admin = new Role(RoleKind.ADMIN.name());
		Role customer = new Role(RoleKind.CUSTOMER.name());
		Role distributor = new Role(RoleKind.DISTRIBUTOR.name());
		roleService.save(admin);
		roleService.save(customer);
		roleService.save(distributor);
	}

	private void fillCustomers() {
		Role customerRole = roleService.findByName(RoleKind.CUSTOMER.getValue());

		Credentials alexCredentials = new Credentials("alex", "alex");
//		customerRole.addCredentials(alexCredentials);
		alexCredentials.addRole(customerRole);
		alexCredentials = customerCredentialsService.save(alexCredentials);
		Address alexAddress = addressService.save(new Address(cityService.findByName("Курск"), "пр-т Победы", "40", "342", "305046"));
		Contact alexContact = contactService.save(new Contact(alexAddress, "123-234-556", "alex@yandex.ru"));

		Credentials danielCredentials = new Credentials("daniel", "daniel");
//		customerRole.addCredentials(danielCredentials);
		danielCredentials.addRole(customerRole);
		danielCredentials = customerCredentialsService.save(danielCredentials);

		Customer alex = new Customer("Alex", alexCredentials, alexContact, null, null);
		customerService.save(alex);

		Customer daniel = new Customer("Daniel", danielCredentials, null, null, null);
		customerService.save(daniel);
	}

	private void fillDistributors() {
		Role distRole = roleService.findByName(RoleKind.DISTRIBUTOR.getValue());
		Credentials distCredentials = new Credentials("dist", "dist");
		distCredentials.addRole(distRole);
		distCredentials = distributorCredentialsService.save(distCredentials);

		City kursk = cityService.findByName("Курск");
		Address kurskStoreAddress = addressService.save(new Address(kursk, "Ленина", "10", null, "305045"));

		Distributor dist = new Distributor(distCredentials,
				contactService.save(new Contact(kurskStoreAddress, "45-54-33", "kursk@ivanov.com")),
				null, null, "ИП Иванов");

		Category electronics = categoryService.findByName("Электроника");
//		List<Product> products = productService.findAllByCategoryAndRegion(electronics.getId(), kursk.getRegion().getId());

		List<Product> products = productService.findAllByCategory(electronics.getId());

		Store kurskStore = new Store(cityService.findByName("Курск"), dist);

		for (Product product : products) {
			kurskStore.addProduct(new StoreItem(product, 100, 10, 1, 10, kurskStore));
		}

		dist.addStore(kurskStore);
		distributorService.save(dist);
	}
}
