<template>
	<svws-ui-input-wrapper :grid="2">
		<svws-ui-content-card :title="`Allgemein`">
			<svws-ui-input-wrapper :grid="2">
				<ui-select label="Betreuende Lehrkraft"
					v-model="model.betreuendeLehrkraft.value"
					:manager="lehrerManager" />
				<ui-select label="Ansprechpartner"
					v-model="model.ansprechpartner.value"
					:manager="ansprechpartnerManager" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-spacing :size="2" />
		<svws-ui-content-card :title="`Details zu ${betrieb?.name}`">
			<template #title>
				<svws-ui-input-wrapper :grid="2">
					<h3 :class="'content-card--headline'"> {{ `Details zu ${betrieb?.name}` }} </h3>
					<svws-ui-button v-if="betrieb !== null" class="rounded-md h-fit"
						type="secondary"
						@click="goToBetrieb(betrieb?.id ?? -1)">
						<span class="icon i-ri-link me-1" />zum Profil
					</svws-ui-button>
				</svws-ui-input-wrapper>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Name" span="2"
					:model-value="betrieb?.name ?? ''"
					readonly />
				<svws-ui-text-input placeholder="Namensergänzung"
					:model-value="betrieb?.nameZusatz"
					readonly />
				<svws-ui-text-input placeholder="Branche"
					:model-value="betrieb?.branche"
					readonly />
				<svws-ui-text-input placeholder="Telefon"
					:model-value="betrieb?.telefon1"
					readonly />
				<svws-ui-text-input placeholder="2. Telefon"
					:model-value="betrieb?.telefon1"
					readonly />
				<svws-ui-text-input placeholder="Straße und Hausnummer"
					:model-value="adresse"
					readonly />
				<svws-ui-text-input placeholder="Ort"
					:model-value="ort?.ortsname"
					readonly />
				<svws-ui-text-input placeholder="Fax"
					:model-value="betrieb?.fax"
					readonly />
				<svws-ui-text-input placeholder="E-Mail"
					:model-value="betrieb?.eMail"
					readonly />
				<svws-ui-textarea-input placeholder="Bemerkungen" :span="'full'"
					:model-value="betrieb?.bemerkungen"
					readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</svws-ui-input-wrapper>
</template>

<script setup lang="ts">

	import type { Betrieb, OrtKatalogEintrag, SchuelerBetrieb } from "@core";
	import { AdressenUtils } from "@core";
	import type { SchuelerBetriebeManager } from "@ui";
	import { SelectManager } from "@ui";
	import { computed } from "vue";
	import { SchuelerBetriebeModelProxy } from "~/components/schueler/betriebe/modelproxy/SchuelerBetriebeModelProxy";

	const props = defineProps<{
		selectedBetrieb: SchuelerBetrieb;
		manager: () => SchuelerBetriebeManager;
		patch: (id: number, data: Partial<SchuelerBetrieb>) => Promise<boolean>;
		goToBetrieb: (idBetrieb: number) => Promise<void>;
	}>();

	const betrieb = computed<Betrieb | null>(() => props.manager().betriebeById.get(model.betrieb.value?.id ?? -1) ?? null);
	const ort = computed<OrtKatalogEintrag | null>(() => props.manager().orteById.get(betrieb.value?.idOrt ?? -1) ?? null);
	const lehrer = computed(() => props.manager().lehrerById.values());
	const ansprechpartner = computed(() => props.manager().ansprechpartnerById.values());
	const model = new SchuelerBetriebeModelProxy(() => props.selectedBetrieb, props.manager, (data) => props.patch(props.selectedBetrieb.id, data));

	const adresse = computed(() => AdressenUtils.combineStrasse(
		betrieb.value?.strasse ?? null,
		betrieb.value?.hausnummer ?? null,
		betrieb.value?.hausnummerZusatz ?? null));

	const lehrerManager = new SelectManager({
		options: lehrer,
		optionDisplayText: v => v.kuerzel,
		selectionDisplayText: v => v.kuerzel,
	});

	const ansprechpartnerManager = new SelectManager({
		options: ansprechpartner,
		optionDisplayText: v => v.name ?? '—',
		selectionDisplayText: v => v.name ?? '—',
	});

</script>
