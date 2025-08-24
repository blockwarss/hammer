# HammerUpgrades (Addon pour Hammer)

Addon pour le plugin `Hammer` existant (le JAR d'origine).
Ajoute des **Hammer Upgrade** (Basic, Rare, Epic) **sans toucher** à la logique du Hammer d'origine :
- Basic → Efficiency II
- Rare → Efficiency IV
- Epic → conversion en DIAMOND_PICKAXE + Efficiency III
- Non cumulable : une fois appliqué, plus d'autres upgrades
- Ajoute au lore : `Rareté: <type>` (en conservant le reste du lore)

Build :
```bash
mvn -B -DskipTests package
```
JAR : `target/hammer-upgrades-addon-1.0.1.jar`