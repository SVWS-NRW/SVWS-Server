import { createRouter, createWebHistory } from 'vue-router'


const routes = [
	{ path: '/README', component: () => import('./README.story.md') },
	{ path: '/Install', component: () => import('./Install.story.md') },
	{ path: '/Icons', component: () => import('./ui/SvwsDocsIcons.story.vue') },
	{ path: '/Colors', component: () => import('./ui/SvwsDocsUiColors.story.vue') },
	{ path: '/Changelog', component: () => import('./Changelog.story.md') },
	{ path: '/Deprecated', component: () => import('./Deprecated.story.md') },
	{ path: '/ui/ToDo', component: () => import('./ui/SvwsUiTodo.story.vue') },
	{ path: '/ui/Badge', component: () => import('./ui/SvwsUiBadge.story.vue') },
	{ path: '/ui/Tooltip', component: () => import('./ui/SvwsUiTooltip.story.vue') },
	{ path: '/ui/Spinner', component: () => import('./ui/SvwsUiSpinner.story.vue') },
	{ path: '/ui/Avatar', component: () => import('./ui/SvwsUiAvatar.story.vue') },
	{ path: '/ui/Notifications', component: () => import('./ui/SvwsUiNotifications.story.vue') },
	{ path: '/controls/Select', component: () => import('./ui/controls/select/UiSelect.story.vue') },
	{ path: '/controls/Button', component: () => import('./ui/controls/SvwsUiButton.story.vue') },
	{ path: '/controls/ButtonSelect', component: () => import('./ui/controls/SvwsUiButtonSelect.story.vue') },
	{ path: '/controls/Checkbox', component: () => import('./ui/controls/SvwsUiCheckbox.story.vue') },
	{ path: '/controls/NumberInput', component: () => import('./ui/controls/SvwsUiInputNumber.story.vue') },
	{ path: '/controls/TextInput', component: () => import('./ui/controls/SvwsUiTextInput.story.vue') },
	{ path: '/controls/Textarea', component: () => import('./ui/controls/SvwsUiTextareaInput.story.vue') },
	{ path: '/controls/MultiSelect-alt', component: () => import('./ui/controls/SvwsUiMultiSelect.story.vue') },
	{ path: '/controls/Select-alt', component: () => import('./ui/controls/SvwsUiSelect.story.vue') },
	{ path: '/controls/Radio', component: () => import('./ui/controls/SvwsUiRadioOption.story.vue') },
	{ path: '/controls/TableGrid', component: () => import('./ui/controls/tablegrid/UiTableGrid.story.md') },
	{ path: '/layout/AppLayout', component: () => import('./ui/layout/SvwsUiAppLayout.story.vue') },
	{ path: '/layout/ContentCard', component: () => import('./ui/layout/SvwsUiContentCard.story.vue') },
	{ path: '/layout/DashboardTile', component: () => import('./ui/layout/SvwsUiDashboardTile.story.vue') },
	{ path: '/layout/Header', component: () => import('./ui/layout/SvwsUiHeader.story.vue') },
	{ path: '/layout/Table', component: () => import('./ui/layout/SvwsUiTable.story.vue') },
	{ path: '/layout/Card', component: () => import('./ui/layout/UiCard.story.vue') },
	{ path: '/modal/Modal', component: () => import('./ui/modal/SvwsUiModal.story.vue') },
	{ path: '/navigation/Menu', component: () => import('./ui/nav/SvwsUiMenu.story.vue') },
	{ path: '/navigation/MenuItem', component: () => import('./ui/nav/SvwsUiMenuItem.story.vue') },
	{ path: '/navigation/TabBar', component: () => import('./ui/nav/SvwsUiTabBar.story.vue') },
	{ path: '/navigation/AppLayout', component: () => import('./ui/nav/SvwsUiMenu.story.vue') },
	{ path: '/gost/laufbahnplanung/Infos', component: () => import('./components/gost/laufbahnplanung/SLaufbahnplanungInformationen.story.vue') },
]

const router = createRouter({ history: createWebHistory(), routes });
export default router;