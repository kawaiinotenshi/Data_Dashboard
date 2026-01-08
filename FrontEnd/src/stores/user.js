import { createEntityStore } from './useEntityStore'
import api from '../api'

export const useUserStore = createEntityStore(
  'user',
  api.user.list,
  '用户',
  'userList',
  'fetchUserList',
  'clearUserList'
)
