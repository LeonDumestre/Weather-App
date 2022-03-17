package fr.iut.kotlin.androidproject.utils

import fr.iut.kotlin.androidproject.data.CommuneLocation

object CommuneSingleton {
    var list : List<CommuneLocation> = listOf(
        CommuneLocation("Brest", 48.390394, -4.486076),
        CommuneLocation("Cherbourg-en-Cotentin", 49.6337308, -1.622137),
        CommuneLocation("Rennes", 48.117266, -1.6777926),
        CommuneLocation("Nantes", 47.218371, -1.553621),
        CommuneLocation("La Rochelle", 46.160329, -1.151139),
        CommuneLocation("Bordeaux", 44.837789, -0.57918),
        CommuneLocation("Biarritz", 43.4831519, -1.558626),
        CommuneLocation("Rouen", 49.443232, 1.099971),
        CommuneLocation("Alençon", 48.432856, 0.091266),
        CommuneLocation("Tours", 47.394144, 0.68484),
        CommuneLocation("Limoges", 45.833619, 1.261105),
        CommuneLocation("Tarbes", 43.232951, 0.078082),
        CommuneLocation("Toulouse", 43.604652, 1.444209),
        CommuneLocation("Lille", 50.62925, 3.057256),
        CommuneLocation("Amiens", 49.894067, 2.295753),
        CommuneLocation("Paris", 48.856614, 2.3522219),
        CommuneLocation("Bourges", 47.081012, 2.398782),
        CommuneLocation("Aurillac", 44.930953, 2.444997),
        CommuneLocation("Perpignan", 42.6886591, 2.8948332),
        CommuneLocation("Reims", 49.258329, 4.031696),
        CommuneLocation("Auxerre", 47.798202, 3.573781),
        CommuneLocation("Vichy", 46.131859, 3.425488),
        CommuneLocation("Montpellier", 43.610769, 3.876716),
        CommuneLocation("Chaumont", 48.113748, 5.1392559),
        CommuneLocation("Chalon-sur-Saône", 46.780764, 4.853947),
        CommuneLocation("Lyon", 45.764043, 4.835659),
        CommuneLocation("Montélimar", 44.556944, 4.749496),
        CommuneLocation("Marseille", 43.296482, 5.36978),
        CommuneLocation("Strasbourg", 48.5734053, 7.7521113),
        CommuneLocation("Metz", 49.1193089, 6.1757156),
        CommuneLocation("Belfort", 47.639674, 6.863849),
        CommuneLocation("Bourg-Saint-Maurice", 45.618598, 6.769548),
        CommuneLocation("Gap", 44.566667, 6.083333),
        CommuneLocation("Nice", 43.7101728, 7.2619532),
        CommuneLocation("Ajaccio", 41.919229, 8.738635)
    )

    fun getCommuneLocation(commune: String): CommuneLocation {
        for (item in list) {
            if (item.commune == commune)
                return item
        }
        return CommuneLocation("error", 0.0, 0.0)
    }

}