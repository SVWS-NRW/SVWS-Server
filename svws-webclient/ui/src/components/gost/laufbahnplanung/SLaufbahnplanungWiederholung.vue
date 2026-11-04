<template>
	<svws-ui-button v-if="!hatWiederholung && hatUpdateKompetenz" type="transparent" @click="showAnlegen = true" :disabled="halbjahrNeu === null"
		:title="(halbjahrNeu === null) ? 'Im folgenden Schuljahresabschnitt kann für diese Laufbahn keine Wiederholung geplant werden.' : undefined">
		<span class="icon-sm i-ri-arrow-go-back-line" />Wiederholung planen…
	</svws-ui-button>
	<!-- Die Buttons kennen keinen orangen Typ, daher färben Tailwind-Klassen am umgebenden Element beide Teile des Dropdown-Buttons ein -->
	<div v-if="zeigeAuswahl" class="flex [&_button]:bg-ui-caution! [&_button]:text-ui-oncaution! [&_button]:border-ui-caution! [&_button:hover]:bg-ui-caution-hover!
		[&_.icon]:icon-ui-oncaution! [&_.icon-sm]:icon-ui-oncaution!">
		<svws-ui-button-select type="secondary" :dropdown-actions="aktionen" :default-action="anzeige" no-default>
			<template #icon> <span class="icon-sm i-ri-arrow-go-back-fill" /> </template>
		</svws-ui-button-select>
	</div>
	<svws-ui-modal v-model:show="showAnlegen" size="small">
		<template #modalTitle>Wiederholung planen</template>
		<template #modalDescription>
			<p>
				Für {{ gostLaufbahnplanungState.schueler.vorname }} {{ gostLaufbahnplanungState.schueler.nachname }} wird eine Wiederholung ab
				{{ halbjahrNeu?.kuerzel ?? '—' }} im Abiturjahrgang {{ abiturjahrNeu ?? '—' }} geplant.
			</p>
			<ul class="list-disc pl-5 mt-2 text-left">
				<li>Die bisherigen Fachwahlen werden als Grundlage für die neue Planung kopiert. Die GKL-Wahlen bleiben leer.</li>
				<li>Bis zum Einstieg zählt die Laufbahn zu beiden Jahrgängen. Die aktuelle Planung bleibt erhalten und kann weiter bearbeitet werden.</li>
				<li>Lernabschnitte und Leistungsdaten bleiben unverändert.</li>
				<li>Sobald der Wechsel in den neuen Jahrgang vollzogen ist, ersetzt die zukünftige Planung die aktuelle.</li>
			</ul>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showAnlegen = false">Abbrechen</svws-ui-button>
			<svws-ui-button @click="anlegen">Wiederholung planen</svws-ui-button>
		</template>
	</svws-ui-modal>
	<svws-ui-modal v-model:show="showEntfernen" size="small" type="danger">
		<template #modalTitle>Geplante Wiederholung zurücknehmen</template>
		<template #modalDescription>
			Die zukünftige Planung für den Abiturjahrgang {{ gostLaufbahnplanungState.wiederholung.abiturjahr }} wird gelöscht. Die Laufbahn gehört dann
			nur noch zum Abiturjahrgang {{ gostLaufbahnplanungState.wiederholung.abiturjahrLernabschnitt ?? '—' }}. Die aktuelle Planung bleibt unverändert.
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showEntfernen = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="danger" @click="entfernen">Zurücknehmen</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";

	import type { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
	import { GostAbiturjahrUtils } from "@core/core/utils/gost/GostAbiturjahrUtils";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useGostLaufbahnplanungState } from "@ui/states/GostLaufbahnplanungState";

	const props = defineProps<{
		hatUpdateKompetenz: boolean;
	}>();

	const gostLaufbahnplanungState = useGostLaufbahnplanungState();
	const abschnittState = useAbschnittState();

	const showAnlegen = ref<boolean>(false);
	const showEntfernen = ref<boolean>(false);

	const hatWiederholung = computed<boolean>(() => gostLaufbahnplanungState.wiederholung.abiturjahr !== null);

	// Nach dem vollzogenen Wechsel gilt die zukünftige Planung wie jede andere Laufbahnplanung, daher entfällt der Button
	const zeigeAuswahl = computed<boolean>(() => hatWiederholung.value && !gostLaufbahnplanungState.wiederholung.wechselVollzogen);

	// Eine Wiederholung führt immer in den folgenden Abiturjahrgang
	const abiturjahrNeu = computed<number | null>(() => {
		const abiturjahr = gostLaufbahnplanungState.wiederholung.abiturjahrLernabschnitt;
		return (abiturjahr === null) ? null : abiturjahr + 1;
	});

	// Der Einstieg erfolgt mit dem Halbjahr, das auf den Schuljahresabschnitt des Schülers folgt
	const halbjahrNeu = computed<GostHalbjahr | null>(() => {
		const idAbschnitt = gostLaufbahnplanungState.schuelerOrNull?.idSchuljahresabschnitt;
		const abschnitt = (idAbschnitt === undefined) ? null : abschnittState.getOrNull(idAbschnitt);
		if ((abschnitt === null) || (abiturjahrNeu.value === null)) {
			return null;
		}
		return GostAbiturjahrUtils.getWiederholungHalbjahr(abiturjahrNeu.value, abschnitt.schuljahr, abschnitt.abschnitt);
	});

	// Die Beschriftung nennt die angezeigte Planung, ein Klick öffnet nur das Menü
	const anzeige = computed(() => ({
		text: gostLaufbahnplanungState.aktuellePlanung
			? `Geplante Wiederholung: Aktuelle Planung (Abitur ${gostLaufbahnplanungState.wiederholung.abiturjahrLernabschnitt ?? '—'})`
			: `Geplante Wiederholung: Zukünftige Planung (Abitur ${gostLaufbahnplanungState.wiederholung.abiturjahr})`,
		action: () => {},
	}));

	const aktionen = computed(() => {
		const wiederholung = gostLaufbahnplanungState.wiederholung;
		const result = [];
		if (gostLaufbahnplanungState.aktuellePlanung) {
			result.push({ text: `Zukünftige Planung anzeigen (Abitur ${wiederholung.abiturjahr})`,
				action: () => gostLaufbahnplanungState.setAktuellePlanung(false) });
		} else {
			result.push({ text: `Aktuelle Planung anzeigen (Abitur ${wiederholung.abiturjahrLernabschnitt ?? '—'})`,
				action: () => gostLaufbahnplanungState.setAktuellePlanung(true) });
		}
		if (props.hatUpdateKompetenz) {
			result.push({ text: "Geplante Wiederholung zurücknehmen…", action: () => {
				showEntfernen.value = true;
			} });
		}
		return result;
	});

	async function anlegen() {
		showAnlegen.value = false;
		if (!props.hatUpdateKompetenz) {
			return;
		}
		await gostLaufbahnplanungState.erstelleWiederholung();
	}

	async function entfernen() {
		showEntfernen.value = false;
		if (!props.hatUpdateKompetenz) {
			return;
		}
		await gostLaufbahnplanungState.entferneWiederholung();
	}

</script>
