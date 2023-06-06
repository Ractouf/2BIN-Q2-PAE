import Navigate from '../../Router/Navigate';
import { clearAuthenticatedUser } from '../../../utils/auths';

const Logout = () => {
  clearAuthenticatedUser();
  Navigate('/');
};

export default Logout;
