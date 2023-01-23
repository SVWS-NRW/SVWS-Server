import "./assets/styles/index.css";
import { App, Plugin } from "vue";
import SmartTable from "vuejs-smart-table";

export const SvwsUiPlugin: Exclude<Plugin["install"], undefined> = function install(
	app: App
) {
	// TODO: statt dem plugin sollten wir auch hier die Komponenten einzeln exportieren.
	// Dazu müssen wir in der smartTable Bib aber noch eine kleine Anpassung vornehmen.
	// https://github.com/antony-k1208/vuejs-smart-table/pull/1
	app.use(SmartTable);
};

// Components
export {
	SvwsUiButton,
	SvwsUiCheckbox,
	SvwsUiDropdown,
	SvwsUiDropdownItem,
	SvwsUiDropdownWithAction,
	SvwsUiMultiSelect,
	SvwsUiProgressBar,
	SvwsUiRadioGroup,
	SvwsUiRadioOption,
	SvwsUiSelectInput,
	SvwsUiTabBar,
	SvwsUiTabButton,
	SvwsUiTabPanel,
	SvwsUiTextareaInput,
	SvwsUiTextInput,
	SvwsUiToggle,
	SvwsUiAvatar,
	SvwsUiContentCard,
	SvwsUiHeader,
	SvwsUiIcon,
	SvwsUiModal,
	SvwsUiNotification,
	SvwsUiOverlay,
	SvwsUiAppLayout,
	SvwsUiSidebarMenu,
	SvwsUiSidebarMenuHeader,
	SvwsUiSidebarMenuItem,
	SvwsUiSecondaryMenu,
	SvwsUiBadge,
	SvwsUiTooltip,
	SvwsUiPopover,
	SvwsUiTable,
	SvwsUiRouterTabBar,
	SvwsUiRouterTabBarButton,
	SvwsUiRouterVerticalTabBar,
	SvwsUiDragData,
	SvwsUiDropData,
	SvwsUiDataTable
} from './components'

export type { DataTableColumn, DataTableItem } from './components/Layout/Table/types'
