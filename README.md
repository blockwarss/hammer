# HammerPlugin (Paper 1.21)

Hammer 3×3 + système d'**Upgrades** (Basic, Rare, Epic).
- **Basic** → Efficiency II
- **Rare** → Efficiency IV
- **Epic** → conversion en DIAMOND_PICKAXE + Efficiency III
- Les upgrades sont **non cumulables**. Une fois fusionné, le Hammer **n'accepte plus** d'autres upgrades.
- Lore ajouté : `Rareté: <type>`

## Build
```bash
mvn -B -DskipTests package
```
Le JAR se trouve dans `target/hammer-plugin-1.0.0.jar`.

## Commandes
- `/hammer give [joueur]`
- `/hammerupgrade <basic|rare|epic> [joueur]`

## Utilisation en jeu
1. Hammer en **main droite**  
2. Upgrade en **main gauche**  
3. **Sneak + clic droit** pour fusionner
