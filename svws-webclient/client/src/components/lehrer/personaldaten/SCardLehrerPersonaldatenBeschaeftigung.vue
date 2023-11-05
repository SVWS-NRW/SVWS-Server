<template>
	<svws-ui-content-card title="Beschäftigungsdaten">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-select title="Rechtsverhältnis" v-model="rechtsverhaeltnis" :items="LehrerRechtsverhaeltnis.values()"
				:item-text="(i: LehrerRechtsverhaeltnis) =>i.daten.text" />
			<svws-ui-select title="Beschäftigungsart" v-model="beschaeftigungsart" :items="LehrerBeschaeftigungsart.values()"
				:item-text="(i: LehrerBeschaeftigungsart) =>i.daten.text" />
			<svws-ui-input-number placeholder="Pflichtstundensoll" :model-value="personalabschnittsdaten?.pflichtstundensoll ?? 0.0" @change="pflichtstundensoll => patchAbschnittsdaten({ pflichtstundensoll: pflichtstundensoll }, personalabschnittsdaten?.id ?? -1)" />
			<svws-ui-select title="Einsatzstatus" v-model="einsatzstatus" :items="LehrerEinsatzstatus.values()"
				:item-text="(i: LehrerEinsatzstatus) =>i.daten.text" />
			<svws-ui-text-input placeholder="Stammschule" :model-value="personaldaten.stammschulnummer" @change="stammschulnummer => patch({stammschulnummer})" type="text" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { type Schuljahresabschnitt, type LehrerListeManager, type LehrerPersonalabschnittsdaten, type LehrerPersonaldaten,
		LehrerBeschaeftigungsart, LehrerEinsatzstatus, LehrerRechtsverhaeltnis } from "@core";

	const props = defineProps<{
		lehrerListeManager: () => LehrerListeManager;
		patch: (data : Partial<LehrerPersonaldaten>) => Promise<void>;
		patchAbschnittsdaten: (data : Partial<LehrerPersonalabschnittsdaten>, id : number) => Promise<void>;
		aktAbschnitt: Schuljahresabschnitt;
	}>();

	const personaldaten = computed<LehrerPersonaldaten>(() => props.lehrerListeManager().personalDaten());
	const personalabschnittsdaten = computed<LehrerPersonalabschnittsdaten | null>(() => props.lehrerListeManager().getAbschnittBySchuljahresabschnittsId(props.aktAbschnitt.id));

	const rechtsverhaeltnis = computed<LehrerRechtsverhaeltnis | undefined>({
		get(): LehrerRechtsverhaeltnis | undefined {
			return LehrerRechtsverhaeltnis.values().find(r => r.daten.kuerzel === personalabschnittsdaten.value?.rechtsverhaeltnis);
		},
		set(val: LehrerRechtsverhaeltnis | undefined) {
			if (personalabschnittsdaten.value != null)
				void props.patchAbschnittsdaten({ rechtsverhaeltnis: val?.daten.kuerzel }, personalabschnittsdaten.value.id);
		}
	});

	const beschaeftigungsart = computed<LehrerBeschaeftigungsart | undefined>({
		get(): LehrerBeschaeftigungsart | undefined {
			return LehrerBeschaeftigungsart.values().find(r => r.daten.kuerzel === personalabschnittsdaten.value?.beschaeftigungsart);
		},
		set(val: LehrerBeschaeftigungsart | undefined) {
			if (personalabschnittsdaten.value != null)
				void props.patchAbschnittsdaten({ beschaeftigungsart: val?.daten.kuerzel }, personalabschnittsdaten.value.id);
		}
	});

	const einsatzstatus = computed<LehrerEinsatzstatus | undefined>({
		get(): LehrerEinsatzstatus | undefined {
			return LehrerEinsatzstatus.values().find(r => r.daten.kuerzel === personalabschnittsdaten.value?.einsatzstatus);
		},
		set(val: LehrerEinsatzstatus | undefined) {
			if (personalabschnittsdaten.value != null)
				void props.patchAbschnittsdaten({ einsatzstatus: val?.daten.kuerzel }, personalabschnittsdaten.value.id);
		}
	});

</script>
