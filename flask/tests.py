from unittest import TestCase, main as unittest_main, mock
from bson.objectid import ObjectId
from app import app


sample_user = {
    'Id': ObjectId('5d55cffc4a3d4031f42827a3'),
    'Username': 'LeTest',
    'Mail': 'sendme@gmail.com',
    'password': 'test123',
    'Qrcode': 'TODO'
}



sample_food = {
    'Utilisateur' : sample_user,
    'Nom': 'Danette Vanille',
    'Marque': 'Danone',
    'Quantité': 4,
    'ingredients': [
        'lait entier',
        'lait écrémé reconstitué à base de lait en poudre',
        'sucre',
        'crème',
        'lait écrémé concentré ou en poudre',
        'épaississants (amidon modifié, carraghénanes)',
        'perméat de petit lait (lactosérum) en poudre',
        'amidon',
        'arôme (lait)',
        'colorant (bêta-carotène)'
    ],
    'Date de péremption': '20/12/2021',
    'Valeurs nutrionelles pour 100g': {
        'Energie': '107 kcal',
        'Matières grasses': '3,0g',
        'Glucides': '17,1g',
        'Proteines': '3g',
        'Sel': '0,14g'
    },
    'Poids': '125g',
    'Lieu': 'Frigo'

}

class PlaylistsTests(TestCase):
    """Flask tests.
    https://makeschool.org/mediabook/oa/tutorials/playlistr-video-playlists-with-flask-and-mongodb-1c/adding-tests/
    """

    def setUp(self):
        """Stuff to do before every test."""

        # Get the Flask test client
        self.client = app.test_client()

        # Show Flask errors that happen during tests
        app.config['TESTING'] = True

    def test_idex(self):
        result = self.client.get('/')
        self.assertEqual(result.status, '200 OK')
        page_content = result.get_data(as_text=True)
        self.assertIn('Hello world!', page_content)





if __name__ == '__main__':
    unittest_main()

