import HomePage from '../Pages/HomePage';
import LoginPage from '../Pages/Authentification/LoginPage';
import RegisterPage from '../Pages/Authentification/RegisterPage';
import Logout from '../Pages/Authentification/Logout';
import IndexPage from '../Pages/Dashboard/IndexPage';
import ObjectsPage from '../Pages/Dashboard/Objects/ObjectsPage';
import UsersPage from '../Pages/Dashboard/Users/UsersPage';
import ObjectEditPage from '../Pages/Dashboard/Objects/ObjectEditPage';
import ListProposedObjectPage from '../Pages/Dashboard/Objects/ListProposedObjectPage';
import ProfileIndexPage from '../Pages/Profile/ProfileIndexPage';
import ProfileEditPage from '../Pages/Profile/ProfileEditPage';
import ObjectPage from '../Pages/Objects/ObjectPage';
import DetailedUserPage from '../Pages/Dashboard/Users/DetailedUserPage';
import ProposePage from '../Pages/Objects/ProposePage';
import AddAvailabilityPage from '../Pages/Dashboard/Availabilities/AddAvailabilityPage';

const routes = {
  '/': HomePage,
  '/login': LoginPage,
  '/register': RegisterPage,
  '/logout': Logout,
  '/dashboard': IndexPage,
  '/dashboard/users': UsersPage,
  '/dashboard/objects': ObjectsPage,
  '/dashboard/objects/edit': ObjectEditPage,
  '/dashboard/objects/proposed': ListProposedObjectPage,
  '/profile': ProfileIndexPage,
  '/profile/edit': ProfileEditPage,
  '/object': ObjectPage,
  '/object/propose': ProposePage,
  '/dashboard/user': DetailedUserPage,
  '/dashboard/availabilities': AddAvailabilityPage,
};

export default routes;
