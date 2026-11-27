<template>
	<div v-if="lehrer !== undefined" class="page page-grid-cards">
		<svws-ui-content-card class="col-span-full">
			<template #title>
				<div class="flex flex-wrap items-center gap-2"><h3 class="content-card--headline" title="Daten der Lehrkraft">Daten der Lehrkraft</h3><svws-ui-badge type="light" title="ID" class="font-mono" size="big"> UV-ID: {{ lehrer.id }} </svws-ui-badge><svws-ui-badge type="light" title="ID" class="font-mono" size="big"> K-ID: {{ lehrer.idKLehrer }} </svws-ui-badge></div>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<div class="flex gap-2 items-end col-span-2 max-w-96">
					<svws-ui-text-input :readonly="!hatKompetenzAendern || lehrer.idKLehrer !== null"
						:disabled="loading"
						placeholder="Kürzel"
						:model-value="form.kuerzel"
						@change="kuerzel => patch({ kuerzel: kuerzel ?? undefined })"
						class="w-full" />
					<svws-ui-button v-if="lehrer.idKLehrer !== null"
						type="icon"
						@click="gotoLehrer(lehrer.idKLehrer)"
						title="Lehrerdaten öffnen">
						<span class="icon i-ri-link" />
					</svws-ui-button>
				</div>
				<svws-ui-text-input :readonly="!hatKompetenzAendern || lehrer.idKLehrer !== null"
					:disabled="loading"
					placeholder="Nachname"
					:model-value="form.nachname"
					@change="nachname => patch({ nachname: nachname ?? undefined })" />
				<svws-ui-text-input :readonly="!hatKompetenzAendern || lehrer.idKLehrer !== null"
					:disabled="loading"
					placeholder="Vorname"
					:model-value="form.vorname"
					@change="vorname => patch({ vorname: vorname ?? undefined })" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<svws-ui-content-card title="Unterrichtsfächer" class="col-span-full pt-5">
			<s-uv-lehrer-unterrichtsfaecher :hat-update-kompetenz="hatKompetenzAendern && !loading"
				:lehrer-unterrichtsfaecher="getLehrerUnterrichtsfaecher"
				:map-faecher
				:patch-lehrer-unterrichtsfach="state.grunddaten.patchLehrerUnterrichtsfach"
				:add-lehrer-unterrichtsfach
				:remove-lehrer-unterrichtsfach="state.grunddaten.removeLehrerUnterrichtsfach" />
		</svws-ui-content-card>

		<s-uv-lehrer-pflichtstundensoll v-model:loading="loading" :lehrer :hat-kompetenz-aendern :patch-gueltigkeit />
		<s-uv-lehrer-anrechnungsstunden v-model:loading="loading" :lehrer :hat-kompetenz-aendern :patch-gueltigkeit />
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";

	import type { LehrerUnterrichtsfach } from "@core/core/data/lehrer/LehrerUnterrichtsfach";
	import { UvLehrer } from "@core/core/data/uv/UvLehrer";
	import type { UvLehrerAnrechnungsstunden } from "@core/core/data/uv/UvLehrerAnrechnungsstunden";
	import type { UvLehrerPflichtstundensoll } from "@core/core/data/uv/UvLehrerPflichtstundensoll";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useUvState } from "@ui/states/UvState";

	import SUvLehrerAnrechnungsstunden from "./SUvLehrerAnrechnungsstunden.vue";
	import SUvLehrerPflichtstundensoll from "./SUvLehrerPflichtstundensoll.vue";
	import SUvLehrerUnterrichtsfaecher from "./SUvLehrerUnterrichtsfaecher.vue";

	const props = defineProps<{
		lehrer: UvLehrer | undefined;
		gotoLehrer: (id: number) => Promise<void>;
	}>();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);

	// Stammdaten

	const form = computed(() => props.lehrer ?? new UvLehrer());

	async function patch(changes: Partial<UvLehrer>): Promise<void> {
		if ((props.lehrer === undefined) || loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.grunddaten.patchLehrer(props.lehrer.id, changes);
		} finally {
			loading.value = false;
		}
	}

	// Unterrichtsfächer

	const mapFaecher = computed(() => new Map([...state.uvManager.fachdatenGetMenge()].map(fach => [fach.id, fach])));

	function getLehrerUnterrichtsfaecher() {
		if (props.lehrer === undefined) {
			return new ArrayList<LehrerUnterrichtsfach>();
		}
		return state.uvManager.lehrerUnterrichtsfachGetMengeByLehrer(props.lehrer);
	}

	async function addLehrerUnterrichtsfach(eintrag: Partial<LehrerUnterrichtsfach>) {
		if (props.lehrer === undefined || loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.grunddaten.addLehrerUnterrichtsfach(props.lehrer, eintrag);
		} finally {
			loading.value = false;
		}
	}

	// Gemeinsame Gültigkeitsprüfung für die beiden Stundentabellen

	async function patchGueltigkeit(
		item: UvLehrerAnrechnungsstunden | UvLehrerPflichtstundensoll,
		feld: 'gueltigVon' | 'gueltigBis',
		value: string | null
	) {
		const gueltigVon = feld === 'gueltigVon' ? value : item.gueltigVon;
		let gueltigBis = item.gueltigBis;
		if (feld === 'gueltigBis') {
			gueltigBis = value === '' ? null : value;
		}
		if (gueltigVon === null || gueltigVon === '' || (gueltigBis !== null && gueltigBis !== '' && gueltigBis < gueltigVon)) {
			return;
		}
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			if ('anrechnungsgrundKrz' in item) {
				await state.grunddaten.patchAnrechnungsstunde(item.id, { gueltigVon, gueltigBis });
			} else {
				await state.grunddaten.patchPflichtstundensoll(item.id, { gueltigVon, gueltigBis });
			}
		} finally {
			loading.value = false;
		}
	}


</script>
