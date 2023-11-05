<template>
	<svws-ui-content-card title="Mehr- und Minderleistung, Anrechnungsstunden">
		<svws-ui-input-wrapper>
			<svws-ui-select title="Mehrleistung" v-model="mehrleistungsgrund" :items="LehrerMehrleistungArt.values()"
				:item-text="(i: LehrerMehrleistungArt) =>i.daten.text" />
			<svws-ui-select title="Minderleistung" v-model="minderleistungsgrund" :items="LehrerMinderleistungArt.values()"
				:item-text="(i: LehrerMinderleistungArt) =>i.daten.text" />
			<svws-ui-input-number placeholder="Stundensumme" :model-value="personalabschnittsdaten?.pflichtstundensoll ?? 0.0" @change="pflichtstundensoll => patchAbschnittsdaten({ pflichtstundensoll: pflichtstundensoll }, personalabschnittsdaten?.id ?? -1)" />
			<svws-ui-select title="Nicht unterichtliche Tätigkeiten" v-model="anrechnungsgrund" :items="LehrerAnrechnungsgrund.values()"
				:item-text="(i: LehrerAnrechnungsgrund) =>i.daten.text" />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Stammschulnummer" :model-value="personaldaten.stammschulnummer" @change="stammschulnummer => patch({stammschulnummer})" type="text" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { type Schuljahresabschnitt, type LehrerListeManager, type LehrerPersonalabschnittsdaten, type LehrerPersonaldaten,
		LehrerAnrechnungsgrund, LehrerMehrleistungArt, LehrerMinderleistungArt } from "@core";

	const props = defineProps<{
		lehrerListeManager: () => LehrerListeManager;
		patch: (data : Partial<LehrerPersonaldaten>) => Promise<void>;
		patchAbschnittsdaten: (data : Partial<LehrerPersonalabschnittsdaten>, id : number) => Promise<void>;
		aktAbschnitt: Schuljahresabschnitt;
	}>();

	const personaldaten = computed<LehrerPersonaldaten>(() => props.lehrerListeManager().personalDaten());
	const personalabschnittsdaten = computed<LehrerPersonalabschnittsdaten | null>(() => props.lehrerListeManager().getAbschnittBySchuljahresabschnittsId(props.aktAbschnitt.id));

	const mehrleistungsgrund = computed<LehrerMehrleistungArt | undefined>({
		get(): LehrerMehrleistungArt | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerMehrleistungArt.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerMehrleistungArt | undefined) {
			// TODO props.personaldaten.patch({ mehrleistungsgrund: val?.kuerzel });
		}
	});

	const minderleistungsgrund = computed<LehrerMinderleistungArt | undefined>({
		get(): LehrerMinderleistungArt | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerMinderleistungArt.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerMinderleistungArt | undefined) {
			// TODO props.personaldaten.patch({ minderleistungsgrund: val?.kuerzel });
		}
	});

	const anrechnungsgrund = computed<LehrerAnrechnungsgrund | undefined>({
		get(): LehrerAnrechnungsgrund | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerAnrechnungsgrund.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerAnrechnungsgrund | undefined) {
			// TODO props.personaldaten.patch({ anrechnungsgrund: val?.kuerzel });
		}
	});

</script>
