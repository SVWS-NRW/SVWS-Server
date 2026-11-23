<template>
	<svws-ui-modal v-model:show="show" size="small">
		<template #modalTitle>
			Benutzer hinzufügen
		</template>

		<template #modalContent>
			<div class="flex flex-row gap-2 pb-4">
				<svws-ui-button :type="(typ === 'lehrer') ? 'primary' : 'secondary'" :disabled="lehrerOhneBenutzer.length === 0" @click="setTyp('lehrer')"> Lehrer </svws-ui-button>
				<svws-ui-button :type="(typ === 'allgemein') ? 'primary' : 'secondary'" @click="setTyp('allgemein')"> Allgemeiner Benutzer </svws-ui-button>
			</div>
			<svws-ui-input-wrapper v-if="typ === 'lehrer'">
				<svws-ui-checkbox type="toggle" v-model="nurSichtbare">Nur Sichtbare</svws-ui-checkbox>
				<ui-select label="Lehrer" :manager="lehrerSelectManager" :model-value="lehrer" @update:model-value="setLehrer" required :removable="false" />
				<template v-if="vorhandeneCredentials !== undefined">
					<span class="text-ui-caution">
						<span class="icon icon-ui-caution i-ri-alert-line" />
						Für diesen Lehrer sind bereits Anmeldedaten vorhanden (Anmeldename: {{ vorhandeneCredentials.benutzername }}).
					</span>
					<div class="flex flex-row gap-6 items-center">
						<svws-ui-radio-option label="bisherigen Anmeldenamen beibehalten" name="anmeldename"
							:model-value="anmeldenameBeibehalten" @update:model-value="setAnmeldenameBeibehalten(true)" :value="true" />
						<svws-ui-radio-option label="neuen Anmeldenamen verwenden" name="anmeldename"
							:model-value="anmeldenameBeibehalten" @update:model-value="setAnmeldenameBeibehalten(false)" :value="false" />
					</div>
				</template>
				<svws-ui-text-input :model-value="anzeigenameLehrer" placeholder="Anzeigename" readonly />
				<svws-ui-spacing />
				<svws-ui-text-input v-model.trim="modelProxy.proxy.name" placeholder="Anmeldename" :readonly="anmeldenameBeibehalten" :validation="() => modelProxy.getFehler('name')" />
				<svws-ui-text-input v-model.trim="modelProxy.proxy.passwort1" type="password" placeholder="Passwort" />
				<svws-ui-text-input v-model.trim="modelProxy.proxy.passwort2" type="password" placeholder="Passwort wiederholen" :validation="() => modelProxy.getFehler('passwort2')" />
			</svws-ui-input-wrapper>
			<svws-ui-input-wrapper v-else>
				<svws-ui-text-input v-model="modelProxy.proxy.anzeigename" placeholder="Anzeigename (z.B. Tim Taler)" />
				<svws-ui-spacing />
				<svws-ui-text-input v-model.trim="modelProxy.proxy.name" placeholder="Anmeldename (z.B. tim)" :validation="() => modelProxy.getFehler('name')" />
				<svws-ui-text-input v-model.trim="modelProxy.proxy.passwort1" type="password" placeholder="Passwort" />
				<svws-ui-text-input v-model.trim="modelProxy.proxy.passwort2" type="password" placeholder="Passwort wiederholen" :validation="() => modelProxy.getFehler('passwort2')" />
			</svws-ui-input-wrapper>
		</template>

		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button @click="create" :disabled> Weiter </svws-ui-button>
		</template>
	</svws-ui-modal>

	<svws-ui-button type="trash" v-if="showDeleteIcon" @click="deleteBenutzerAllgemein" />

	<svws-ui-button type="icon" @click="show = true" :has-focus>
		<span class="icon i-ri-add-line" />
	</svws-ui-button>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef, watch } from "vue";

	import type { BenutzerListeEintrag } from "@core/core/data/benutzer/BenutzerListeEintrag";
	import type { LehrerCredentialsOhneBenutzerEintrag } from "@core/core/data/benutzer/LehrerCredentialsOhneBenutzerEintrag";
	import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
	import { BenutzerTyp } from "@core/core/types/benutzer/BenutzerTyp";
	import type { List } from "@core/java/util/List";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";

	import { BenutzerModelProxy } from "./BenutzerModelProxy";

	const props = withDefaults(defineProps<{
		showDeleteIcon?: boolean;
		createBenutzerAllgemein: (anmeldename: string, benutzername: string, passwort: string) => Promise<void>;
		createBenutzerLehrer: (idLehrer: number, benutzername: string, passwort: string) => Promise<void>;
		deleteBenutzerAllgemein: () => Promise<void>;
		hasFocus?: boolean;
		mapBenutzer: Map<number, BenutzerListeEintrag>;
		listLehrer: List<LehrerListeEintrag>;
		mapLehrerCredentialsOhneBenutzer: Map<number, LehrerCredentialsOhneBenutzerEintrag>;
	}>(), {
		showDeleteIcon: false,
		hasFocus: false,
	});

	const show = ref<boolean>(false);

	const typ = ref<'lehrer' | 'allgemein'>('lehrer');
	const nurSichtbare = ref<boolean>(true);
	const lehrer = ref<LehrerListeEintrag | null>(null);
	const anmeldenameBeibehalten = ref<boolean>(false);

	const dataNotPatched = shallowRef({ anzeigename: "", name: "", passwort1: "", passwort2: "" });

	const modelProxy = shallowRef(new BenutzerModelProxy(() => dataNotPatched.value, () => props.mapBenutzer.values()));
	watch(() => props.mapBenutzer, () => modelProxy.value = new BenutzerModelProxy(() => dataNotPatched.value, () => props.mapBenutzer.values()), { immediate: false });

	/** Die IDs der Lehrer, denen bereits ein Lehrer-Benutzer zugeordnet ist */
	const idsLehrerMitBenutzer = computed<Set<number>>(() => {
		const result = new Set<number>();
		for (const benutzer of props.mapBenutzer.values()) {
			if (benutzer.typ === BenutzerTyp.LEHRER.id) {
				result.add(benutzer.typID);
			}
		}
		return result;
	});

	/** Die Lehrer, für die ein Lehrer-Benutzer angelegt werden kann, unabhängig von ihrer Sichtbarkeit */
	const lehrerOhneBenutzer = computed<LehrerListeEintrag[]>(() => {
		const result: LehrerListeEintrag[] = [];
		for (const l of props.listLehrer) {
			if (!idsLehrerMitBenutzer.value.has(l.id)) {
				result.push(l);
			}
		}
		return result;
	});

	/** Die Lehrer, die in der Auswahl angeboten werden */
	const lehrerAuswahl = computed<LehrerListeEintrag[]>(() => lehrerOhneBenutzer.value.filter(l => !nurSichtbare.value || l.istSichtbar));

	/**
	 * Legt beim Öffnen des Modals den Benutzertyp fest: Lehrer, wenn noch ein sichtbarer Lehrer ohne Lehrer-Benutzer
	 * vorhanden ist, und ansonsten allgemeiner Benutzer. Sind nur noch nicht sichtbare Lehrer ohne Lehrer-Benutzer
	 * vorhanden, so wird der Filter auf sichtbare Lehrer ausgeschaltet. Beim Schließen werden alle Eingaben verworfen.
	 */
	watch(show, (value) => {
		if (value) {
			const hatSichtbareLehrer = lehrerOhneBenutzer.value.some(l => l.istSichtbar);
			setTyp(hatSichtbareLehrer ? 'lehrer' : 'allgemein');
			nurSichtbare.value = hatSichtbareLehrer || (lehrerOhneBenutzer.value.length === 0);
		} else {
			nurSichtbare.value = true;
			reset();
		}
	});

	const lehrerText = (l: LehrerListeEintrag) => `${l.nachname}, ${l.vorname} (${l.kuerzel})`;

	const lehrerSelectManager = new SelectManager({
		options: lehrerAuswahl,
		optionDisplayText: lehrerText,
		selectionDisplayText: lehrerText,
		sort: (a: LehrerListeEintrag, b: LehrerListeEintrag) => {
			const cmp = a.nachname.localeCompare(b.nachname, "de");
			return (cmp !== 0) ? cmp : a.vorname.localeCompare(b.vorname, "de");
		},
	});

	const vorhandeneCredentials = computed<LehrerCredentialsOhneBenutzerEintrag | undefined>(() =>
		(lehrer.value === null) ? undefined : props.mapLehrerCredentialsOhneBenutzer.get(lehrer.value.id));

	const anzeigenameLehrer = computed<string>(() => (lehrer.value === null) ? "" : `${lehrer.value.vorname} ${lehrer.value.nachname}`);

	const disabled = computed(() => ((typ.value === 'lehrer') && (lehrer.value === null))
		|| (modelProxy.value.proxy.passwort1 !== modelProxy.value.proxy.passwort2) || (modelProxy.value.proxy.passwort1.length === 0) || modelProxy.value.hatFehler());

	function reset() {
		lehrer.value = null;
		anmeldenameBeibehalten.value = false;
		dataNotPatched.value = { anzeigename: "", name: "", passwort1: "", passwort2: "" };
		modelProxy.value.reset();
	}

	function setTyp(value: 'lehrer' | 'allgemein') {
		if (typ.value === value) {
			return;
		}
		typ.value = value;
		reset();
	}

	function setLehrer(value: LehrerListeEintrag | null | undefined) {
		lehrer.value = value ?? null;
		anmeldenameBeibehalten.value = (vorhandeneCredentials.value !== undefined);
		setVorbelegterAnmeldename();
	}

	function setAnmeldenameBeibehalten(value: boolean) {
		anmeldenameBeibehalten.value = value;
		setVorbelegterAnmeldename();
	}

	/**
	 * Belegt den Anmeldenamen mit dem bisherigen Anmeldenamen vor, sofern dieser beibehalten werden soll,
	 * und ansonsten mit dem Kürzel des Lehrers.
	 */
	function setVorbelegterAnmeldename() {
		if (lehrer.value === null) {
			modelProxy.value.proxy.name = "";
			return;
		}
		modelProxy.value.proxy.name = ((vorhandeneCredentials.value !== undefined) && anmeldenameBeibehalten.value)
			? vorhandeneCredentials.value.benutzername
			: lehrer.value.kuerzel;
	}

	async function create() {
		if (typ.value === 'lehrer') {
			if (lehrer.value === null) {
				return;
			}
			await props.createBenutzerLehrer(lehrer.value.id, modelProxy.value.proxy.name, modelProxy.value.proxy.passwort1);
		} else {
			await props.createBenutzerAllgemein(modelProxy.value.proxy.anzeigename, modelProxy.value.proxy.name, modelProxy.value.proxy.passwort1);
		}
		show.value = false;
	}

</script>
