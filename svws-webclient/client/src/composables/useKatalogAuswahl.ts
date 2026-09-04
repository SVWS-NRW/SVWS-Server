import { BenutzerKompetenz } from '@core/core/types/benutzer/BenutzerKompetenz';
import type { List } from '@core/java/util/List';
import type { AuswahlManager } from '@ui/ui/manager/AuswahlManager';
import { ViewType } from '@ui/ui/nav/ViewType';
import { computed } from 'vue';
import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";
import { benutzerStateImpl } from '~/states/BenutzerStateImpl';

interface KatalogManager extends AuswahlManager<number, any, any> {
	searchTerm: string;
	filterNurSichtbar: boolean;
}

interface Katalog {
	id: number;
}

export function useKatalogAuswahl<KType extends Katalog>(props: RouteAuswahlListProps<KatalogManager>) {

	const readonly = computed<boolean>(() => {
		return !benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	});

	const isHinzufuegenView = computed<boolean>(() => {
		return props.activeViewType === ViewType.HINZUFUEGEN;
	});

	const isGruppenprozesseOrHinzufuegenView = computed<boolean>(() => {
		return props.activeViewType === ViewType.GRUPPENPROZESSE || isHinzufuegenView.value;
	});

	const filteredItems = computed<List<KType>>(() => {
		return props.manager().filtered();
	});


	const selectedItems = computed<KType[]>({
		get: () => [...props.manager().liste.auswahl()],
		set: (v: KType[]) => {
			setAuswahl(v);
			void navigateToView();
		},
	});

	const clickedItem = computed<KType | null>({
		get: () => {
			return (!isGruppenprozesseOrHinzufuegenView.value && props.manager().hasDaten()) ? props.manager().auswahl() : null;
		},
		set: (v: KType | null) => void props.gotoDefaultView(v?.id ?? null),
	});

	const searchTerm = computed<string>({
		get: () => props.manager().searchTerm,
		set: (v: string) => {
			props.manager().searchTerm = v;
			void props.setFilter();
		},
	});

	const showOnlyVisible = computed<boolean>({
		get: () => props.manager().filterNurSichtbar,
		set: (value: boolean) => {
			props.manager().filterNurSichtbar = value;
			void props.setFilter();
		},
	});

	const noFilteredItems = computed<boolean>(() => {
		return props.manager().filtered().size() === 0;
	});

	function setAuswahl(items: KType[]) {
		props.manager().liste.auswahlClear();

		for (const item of items) {
			if (props.manager().liste.hasValue(item)) {
				props.manager().liste.auswahlAdd(item);
			}
		}
	}

	async function navigateToView() {
		if (props.manager().liste.auswahlExists()) {
			await props.gotoGruppenprozessView(true);
		} else {
			await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id);
		}
	}

	return {
		readonly,
		filteredItems,
		selectedItems,
		clickedItem,
		isHinzufuegenView,
		isGruppenprozesseOrHinzufuegenView,
		searchTerm,
		showOnlyVisible,
		noFilteredItems,
		navigateToView,
	};
}
