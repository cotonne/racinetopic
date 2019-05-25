package processing

import org.scalatest.{FlatSpec, Matchers}

class NlpPipelineTest extends FlatSpec with Matchers {

  "NlpPipeline" should "transform a text to a normalized bag of words" in {
    val pipeline = NlpPipeline()
    val text = pipeline.normalize("ESTHER",
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
      "esther", "premier", "mois", "temps", "ignorer", "compagne", "chercher", "cherrer", "mémoire", "gloire", "soupirer", "jour", "descendre", "désert", "élise", "bénir", "aider", "joug", "heureux", "oppression", "cacher", "malheur", "souffrir", "assidu", "an", "climat", "benjamin", "sion", "vœux")
  }

  "NlpPipeline" should "transform a text - " in {
    val pipeline = NlpPipeline()
    val text = pipeline.normalize("LA PIETE.",
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
      "droit", "prédestiner", "combattre", "chaleur", "ennemi", "déjà", "artifice", "aurore", "soupir", "enflamme", "point", "discorde", "combat", "arme", "humilier", "étranger", "voir", "splendeur", "déployer", "chercher", "perfide", "confondre", "nation", "briser", "mémoire", "front", "yeux", "écoute", "affreux", "débris", "roi", "perte", "feu", "gloire", "tendre", "animer", "écrit", "piété", "hérésie", "funèbre", "unir", "frémir", "enfer", "jalousie", "vivre", "dévorer", "couronner", "braver", "couvrir", "chérir", "ouvrage", "livre", "jour", "fureur", "pavé", "prosterner", "ferme", "jeter", "zèle", "ténèbre", "sacrer", "grand", "armée", "fille", "venir", "temple", "frontière", "juger", "offrir", "répandre", "querelle", "saint", "allumer", "nom", "fonder", "Rhin", "barrière", "désir", "regarder", "foi", "étendard", "écouter", "soin", "démon", "soutenir", "fort", "auguste", "couchant", "cause", "abandonner", "voix", "rompre", "orgueil", "amour", "écueil", "Main", "prendre", "baiser", "place", "vapeur", "dieu", "aveugle", "respect", "partout", "impuissant", "exemple", "religion", "invariable", "Centre", "édifice", "reconnaitre", "intérêt", "graver", "disperser", "bras")
  }

  "NlpPipeline" should "have good performance" in {
    val pipeline = NlpPipeline()
    val text = pipeline.normalize("ZARÈS",
      """Seigneur, nous sommes seuls.  Que sert de se flatter?
        |Ce zèle que pour lui vous fïtes éclater,
        |Ce soin d'immoler tout à son pouvoir suprême,
        |Entre nous, avaient-ils d'autre objet que vous-même?
        |Et sans chercher plus loin, tous ces Juifs désolés,
        |N'est-ce pas à vous seul que vous les immolez?
        |Et ne craignez-vous point que quelque avis funeste. . . .
        |Enfin la cour nous hait, le peuple nous déteste.
        |Ce Juif même, il le faut confesser malgré moi,
        |Ce Juif, comblé d'honneurs, me cause quelque effroi.
        |Les malheurs sont souvent enchaînés l'un à l'autre,
        |Et sa race toujours fut fatale à la vôtre,
        |De ce léger affront songez à profiter.
        |Peut-être la fortune est prête à vous quitter;
        |Aux plus affreux excès son inconstance passe.
        |Prevénez son caprice avant qu'elle se lasse.
        |Où tendez-vous plus haut?  Je frémis quand je voi
        |Les abîmes profonds qui s'offrent devant moi:
        |La chute désormais ne peut être qu'horrible.
        |Osez chercher ailleurs un destin plus paisible.
        |Regagnez l'Hellespont, et ces bords écartés
        |Où vos aïeux errants jadis furent jetés,
        |Lorsque des Juifs contre eux la vengeance allumée
        |Chassa tout Amalec de la triste Idumée.
        |Aux malices du sort enfin dérobez-vous.
        |Nos plus riches trésors marcheront devant nous.
        |Vous pouvez du départ me laisser la conduite;
        |Surtout de vos enfants j'assurerai la fuite.
        |N'ayez soin cependant que de dissimuler.
        |Contente, sur vos pas vous me verrez voler:
        |La mer la plus terrible et la plus orageuse
        |Est plus sûre pour nous que cette cour trompeuse.
        |Mais à grands pas vers vous je vois quelqu'un marcher.
        |C'est Hydaspe.
      """.stripMargin)
    text shouldBe Set(
      "terrible", "affront", "dérober", "inhumé", "horrible", "voler", "Hellespont", "passer", "prêter", "mer", "léger", "point", "oser", "quitter", "voir", "juif", "chercher", "confesser", "assurer", "affreux", "flatter", "profiter", "combler", "tendre", "marcher", "malice", "cour", "haïr", "frémir", "coi", "songer", "funeste", "suprême", "vengeance", "lasser", "pouvoir", "aïeux", "fuite", "excès", "effroi", "abîme", "jeter", "zèle", "immoler", "grand", "faillir", "seigneur", "triste", "offrir", "seul", "allumer", "chute", "trésor", "fatal", "désolés", "servir", "trompeur", "paisible", "orageux", "caprice", "jadis", "craindre", "avis", "malheur", "soin", "sûr", "écarter", "race", "inconstance", "errant", "cause", "dissimuler", "enchaîner", "éclater", "content", "objet", "fortune", "honneur", "conduit", "regagner", "loin", "sort", "riche", "détester", "profond", "peuple", "destin", "enfant", "bord", "faire", "hydaspe", "départ", "amalec", "chassa", "prévenir"
    )
  }

  "NlpPipeline" should "have good performance - " in {
    val pipeline = NlpPipeline()
    val text = pipeline.normalize("ZARÈS",
      """Vasthi régna longtemps dans son âme offensée.
        |Dans ses nombreux États il fallut donc chercher
        |Quelque nouvel objet qui l'en pût détacher.
        |De l'Inde a l'Hellespont ses esclaves coururent;
        |Les filles de l'Égypte à Suse comparurent;
        |Celles même du Parthe et du Scythe indompté
        |Y briguèrent le sceptre offert à la beauté.""".stripMargin)
    text shouldBe Set(
      "briguer", "offenser", "Hellespont", "comparaitre", "beauté", "sceptre", "chercher", "inde", "falloir", "courir", "scythe", "pouvoir", "fille", "vasthi", "régna", "offrir", "détacher", "âme", "s’use", "objet", "indompté", "nouvel", "esclave", "état", "parthe", "Égypte"
    )
  }

}
