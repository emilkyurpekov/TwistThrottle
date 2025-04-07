package twistthrottle.seeders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import twistthrottle.models.entities.Articles;
import twistthrottle.repositories.ArticleRepository;

import java.util.List;

@Component
@Order(4) // Ensure this runs after other seeders
public class ArticleSeeder implements CommandLineRunner {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleSeeder(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (articleRepository.count() == 0) {
            Articles article1 = new Articles();
            article1.setTitle("Choosing Your First Motorcycle: A Beginner's Guide");
            article1.setSlug("choosing-first-bike");
            article1.setContent(
                    "Embarking on the journey of motorcycle riding is exhilarating! Selecting your first bike is a crucial step. " +
                            "Don't be swayed solely by aesthetics; prioritize practicality and safety. Consider starting with a motorcycle that has a lower engine displacement (e.g., 250cc to 500cc). " +
                            "These bikes are generally lighter, more forgiving, and easier to handle, which is vital while you're building confidence and skill. \n\n" +
                            "Seat height and bike weight are also important factors. Ensure you can comfortably place both feet (or at least the balls of your feet) firmly on the ground when seated. A lighter bike is easier to maneuver at low speeds and pick up if dropped (which can happen to beginners!). " +
                            "Think about your intended riding style: will you be commuting, taking weekend trips, or exploring light trails? Different styles (cruiser, sportbike, standard, dual-sport) excel in different areas. Finally, never underestimate the importance of proper riding gear. A certified helmet, jacket, gloves, sturdy pants, and over-the-ankle boots are non-negotiable investments in your safety."
            );


            Articles article2 = new Articles();
            article2.setTitle("Essential Motorcycle Maintenance for Longevity");
            article2.setSlug("basic-maintenance");
            article2.setContent(
                    "Regular maintenance is key to keeping your motorcycle reliable and safe. It doesn't always require a professional mechanic; many essential checks can be done at home. " +
                            "Start with the T-CLOCS checklist (Tires, Controls, Lights, Oil, Chassis, Stands) before every ride. Check tire pressure when the tires are cold, ensuring they match the manufacturer's recommendations (found in your owner's manual or on a sticker on the bike). Visually inspect tires for wear and tear. \n\n" +
                            "Lubricating and adjusting the chain (if your bike has one) is crucial. A dry or loose chain wears out quickly and can be dangerous. Consult your manual for the correct tension and lubrication intervals. Checking oil levels regularly prevents engine damage. Ensure the bike is level when checking. Brake fluid levels and condition should also be monitored. Look at brake pads for wear. " +
                            "Keep your bike clean; washing it helps you spot potential issues like leaks or loose bolts. Familiarize yourself with your owner's manual – it's the best resource for specific maintenance schedules and procedures for your model."
            );

            Articles article3 = new Articles();
            article3.setTitle("Demystifying Motorcycle Exhaust Systems");
            article3.setSlug("exhaust-systems-explained");
            article3.setContent(
                    "A motorcycle's exhaust system plays a critical role beyond just making noise. It manages the expulsion of burnt gases from the engine, influences performance, affects emissions, and contributes significantly to the bike's sound profile. " +
                            "Stock exhaust systems are designed to meet strict noise and emissions regulations, often prioritizing quietness and compliance over maximum power output. \n\n" +
                            "Aftermarket exhausts come in two main types: slip-ons and full systems. Slip-ons replace only the muffler (the end part), offering moderate changes in sound and aesthetics with minimal performance impact, and usually don't require re-tuning the engine's fuel map. Full systems replace the entire exhaust from the engine headers back. They typically offer more significant weight savings and performance gains but almost always require fuel re-mapping (tuning) to run correctly and avoid engine damage. " +
                            "When choosing an aftermarket exhaust, consider local noise regulations, performance goals, budget, and whether you're prepared for the potential need for engine tuning."
            );


            articleRepository.saveAll(List.of(article1, article2, article3));
        }
    }
}