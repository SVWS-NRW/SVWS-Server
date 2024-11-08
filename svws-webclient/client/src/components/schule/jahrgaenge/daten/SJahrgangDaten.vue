<template>
	<div class="page--content">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Kürzel" :model-value="jahrgangListeManager().daten().kuerzel" @change="patchKuerzel" type="text" />
				<svws-ui-text-input placeholder="Bezeichnung" :model-value="jahrgangListeManager().daten().bezeichnung" @change="bezeichnung => patch({ bezeichnung: bezeichnung ?? '' })" type="text" />
				<svws-ui-select title="Schulgliederung" v-model="schulgliederung" :items="Schulgliederung.getBySchuljahrAndSchulform(schuljahr, schulform)" :item-text="textSchulgliederung" />
				<svws-ui-select title="Statistik-Jahrgang" v-model="statistikjahrgang" :items="Jahrgaenge.getListBySchuljahrAndSchulform(schuljahr, schulform)" :item-text="textStatistikJahrgang" statistics />
				<svws-ui-select title="Folgejahrgang" v-model="idFolgejahrgang" :items="folgejahrgaenge" :item-text="textFolgejahrgang" />
				<svws-ui-input-number placeholder="Anzahl der Restabschnitte" :model-value="jahrgangListeManager().daten().anzahlRestabschnitte" @change="patchRestabschnitte" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { JahrgangsDaten} from "@core";
	import { UserNotificationException, Schulgliederung, Jahrgaenge, DeveloperNotificationException } from "@core";
	import type { JahrgangDatenProps } from "./SJahrgangDatenProps";

	const props = defineProps<JahrgangDatenProps>();

	const folgejahrgaenge = computed<JahrgangsDaten[]>(() =>
		[...props.jahrgangListeManager().liste.list()].filter(j => j.id !== props.jahrgangListeManager().daten().id)
	);

	const idFolgejahrgang = computed<JahrgangsDaten | undefined>({
		get: () => {
			const idFolgejahrgang = props.jahrgangListeManager().daten().idFolgejahrgang;
			if (idFolgejahrgang !== null) {
				const jahrgangsDaten = props.jahrgangListeManager().liste.get(idFolgejahrgang)
				return jahrgangsDaten ?? undefined;
			}
			return undefined;
		},
		set: (value) => void props.patch({ idFolgejahrgang: value?.id })
	});

	const textFolgejahrgang = (jahrgang: JahrgangsDaten) => {
		return (jahrgang.kuerzel !== '') ? jahrgang.kuerzel + ' : ' + jahrgang.bezeichnung : jahrgang.bezeichnung;
	}

	async function patchKuerzel(kuerzel: string | null) {
		const kuerzelToPatch = kuerzel === '' ? null : kuerzel;
		for (const jg of props.jahrgangListeManager().liste.list())
			if (jg.kuerzel === kuerzelToPatch)
				throw new UserNotificationException("Das Kürzel muss eindeutig sein, wird aber bereits für einen anderen Jahrgang verwendet! Es kann daher nicht übernommen werden.");

		await props.patch({ kuerzel: kuerzelToPatch });
	}

	const schulgliederung = computed<Schulgliederung | null>({
		get: () => {
			const kuerzel = props.jahrgangListeManager().daten().kuerzelSchulgliederung;
			if (kuerzel === null)
				return null;
			return Schulgliederung.data().getWertByKuerzel(kuerzel);
		},
		set: (value) => {
			const kuerzel = value?.daten(props.schuljahr)?.kuerzel;
			void props.patch({ kuerzelSchulgliederung : kuerzel ?? null });
		}
	});

	function textSchulgliederung(schulgliederung: Schulgliederung): string {
		return schulgliederung.daten(props.schuljahr)?.kuerzel ?? '—';
	}

	const statistikjahrgang = computed<Jahrgaenge | null>({
		get: () => {
			const kuerzel = props.jahrgangListeManager().daten().kuerzelStatistik;
			return Jahrgaenge.data().getWertByKuerzel(kuerzel);
		},
		set: (value) => {
			const kuerzel = value?.daten(props.schuljahr)?.kuerzel;
			void props.patch({ kuerzelStatistik : kuerzel });
		}
	});

	function textStatistikJahrgang(jahrgang: Jahrgaenge) {
		const jahrgangEintrag = jahrgang.daten(props.schuljahr);
		return (jahrgangEintrag?.kuerzel ?? '—') + ": " + (jahrgangEintrag?.text ?? '—');
	}

	async function patchRestabschnitte(value: number | null) {
		if (value === null)
			throw new DeveloperNotificationException("Die Anzahl der Restabschnitte darf nicht auf null zurückgesetzt werden.");
		if ((value < 0) || (value > 40))
			throw new UserNotificationException("Die Anzahl der Restabschnitte muss in dem Bereich [0; 40] liegen.");
		await props.patch({ anzahlRestabschnitte: value });
	}

</script>
