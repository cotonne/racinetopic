package processing

import org.scalatest.{FlatSpec, Matchers}

class NlpPipelineTest extends FlatSpec with Matchers {

  "NlpPipeline" should "transform a text to a normalized bag of words" in {
    val pipeline = NlpPipeline()
    val text = pipeline.normalize(
      """Est-ce toi, chere Élise?  O jour trois fois heureux!
        |Que béni soit le del qui te rend à mes voeux,
        |Toi qui de Benjamin comme moi descendue,
        |Et qui, d'un même joug souffrant l'oppression,
        |Fus de mes premiers ans la compagne assidue,
        |M'aidais à soupirer les malheurs de Sion.
        |Combien ce temps encore est cher à ma mémoire!
        |Mais toi, de ton Esther ignorais-tu la gloire?
        |Depuis plus de six mois que je te fais chercher,
        |Quel climat, quel désert a donc pu te cacher?""".stripMargin)
    text shouldBe Set(
      "esther", "combien", "encore", "mois", "cher", "assidue", "temps", "ignorer", "compagne", "chercher", "mémoire", "plus", "gloire", "soupirer", "quel", "jour", "descendre", "désert", "élise", "bénir", "trois", "aider", "joug", "heureux", "oppression", "cacher", "foi", "donc", "malheur", "premiers", "souffrir", "chere", "an", "climat", "benjamin", "sion", "rendre", "voeux", "faire")
  }

  "NlpPipeline" should "transform a text - " in {
    val pipeline = NlpPipeline()
    val text = pipeline.normalize(
      """
        |Grand Dieu, que cet ouvrage ait place en ta mémoire.
        |Que tous les soins qu'il prend pour soutenir la gloire
        |Soient gravés de ta main au livre où sont écrits
        |Les noms prédestinés des rois que tu chéris.
        |Tu m'écoutes.  Ma voix ne t'est point étrangère.
        |Je suis la Piété, cette fille si chère,
        |Qui t'offre de ce roi les plus tendres soupirs.
        |Du feu de ton amour j'allume ses desirs.
        |Du zèle qui pour toi l'enflamme et le dévore
        |La chaleur se répand du couchant à l'aurore.
        |Tu le vois tous les jours, devant toi prosterné,
        |Humilier ce front de splendeur couronné,
        |Et confondant l'orgueil par d'augustes exemples,
        |Baiser avec respect le pavé de tes temples.
        |De ta gloire animé, lui seul de tant de rois
        |S'arme pour ta querelle, et combat pour tes droits.
        |Le perfide intérêt, l'aveugle jalousie
        |S'unissent centre toi pour l'affreuse hérésie;
        |La discorde en fureur frémit de toutes parts;
        |Tout semble abandonner tes sacrés etendards,
        |Et l'enfer, couvrant tout de ses vapeurs funèbres,
        |Sur les yeux les plus saints a jeté ses ténèbres.
        |Lui seul, invariable et fondé sur la foi,
        |Ne cherche, ne regarde et n'écoute que toi;
        |Et bravant du demon l'impuissant artifice,
        |De la religion soutient tout l'édifice.
        |Grand Dieu, juge ta cause, et déploie aujourd'hui
        |Ce bras, ce même bras qui combattait pour lui,
        |Lorsque des nations à sa perte animées
        |Le Rhin vit tant de fois disperser les armées.
        |Des mêmes ennemis je reconnais l'orgueil;
        |Ils viennent se briser contre le même écueil.
        |Déjà, rompant partout leurs plus fermes barrières,
        |Du debris de leurs forts il couvre ses frontières.
      """.stripMargin)
    text shouldBe Set(
      "droit", "prédestiner", "combattre", "chaleur", "déjà", "artifice", "aurore", "cher", "soupir", "enflamme", "point", "discorde", "combat", "arme", "humilier", "voir", "splendeur", "déployer", "chéris", "chercher", "perfide", "animé", "sacrés", "confondre", "nation", "briser", "mémoire", "plus", "affreuse", "front", "yeux", "forts", "écoute", "roi", "perte", "feu", "gloire", "tendre", "animer", "écrit", "piété", "hérésie", "funèbre", "unir", "frémir", "etendards", "enfer", "jalousie", "vivre", "même", "dévorer", "couronner", "braver", "couvrir", "tous", "ouvrage", "debris", "livre", "jour", "fureur", "pavé", "ferme", "jeter", "zèle", "ténèbre", "grand", "armée", "devoir", "fille", "venir", "temple", "demon", "frontière", "juger", "offrir", "répandre", "querelle", "seul", "allumer", "nom", "fonder", "Rhin", "barrière", "desirs", "regarder", "foi", "écouter", "soin", "tant", "soutenir", "auguste", "couchant", "cause", "abandonner", "saints", "voix", "rompre", "orgueil", "amour", "écueil", "Main", "prendre", "baiser", "place", "vapeur", "dieu", "aveugle", "respect", "partout", "impuissant", "exemple", "religion", "invariable", "Centre", "édifice", "reconnaitre", "ennemis", "intérêt", "étrangère", "sembler", "prosterné", "graver", "disperser", "bras")
  }

}
