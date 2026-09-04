<template>
	<div class="page page-grid-cards">
		<svws-ui-input-wrapper :grid="1">
			<div v-if="noEntries">
				Aktuell gibt es keine Einträge im Katalog "Einwilligungsarten".
			</div>
			<svws-ui-content-card v-if="hasAbgefragteEinwilligungen" title="Abgefragt" class="col-span-full">
				<div class="space-y-2">
					<div v-for="einwilligung of einwilligungenProxies" :key="einwilligung.proxy.idEinwilligungsart">
						<template v-if="einwilligung.proxy.abgefragt || einwilligung.proxy.status">
							<ui-card :title="getBezeichnungEinwilligungsart(einwilligung.proxy.idEinwilligungsart)" :info="getEinwilligungsstatus(einwilligung.proxy)">
								<div class="w-1/5">
									<p class="text-headline-md mb-1"> Status </p>
								</div>
								<svws-ui-checkbox class="w-2/5" v-model="einwilligung.currentAbgefragt.value" :readonly>
									Abgefragt
								</svws-ui-checkbox>
								<svws-ui-checkbox class="w-2/5" v-model="einwilligung.currentStatus.value" :readonly>
									Zugestimmt
								</svws-ui-checkbox>
							</ui-card>
						</template>
					</div>
				</div>
			</svws-ui-content-card>
			<svws-ui-content-card v-if="hasNichtAbgefragteEinwilligungen" title="Nicht abgefragt" class="col-span-full">
				<div class="space-y-2">
					<div v-for="einwilligung of einwilligungenProxies" :key="einwilligung.proxy.idEinwilligungsart">
						<template v-if="!einwilligung.proxy.abgefragt && !einwilligung.proxy.status">
							<ui-card :title="getBezeichnungEinwilligungsart(einwilligung.proxy.idEinwilligungsart)">
								<div class="w-1/5">
									<p class="text-headline-md mb-1"> Status </p>
								</div>
								<svws-ui-checkbox class="w-2/5" v-model="einwilligung.currentAbgefragt.value" :readonly>
									Abgefragt
								</svws-ui-checkbox>
								<svws-ui-checkbox class="w-2/5" v-model="einwilligung.currentStatus.value" :readonly>
									Zugestimmt
								</svws-ui-checkbox>
							</ui-card>
						</template>
					</div>
				</div>
			</svws-ui-content-card>
		</svws-ui-input-wrapper>
	</div>
</template>

<script setup lang="ts">
	import { computed } from "vue";
	import type { SchuelerEinwilligungenProps } from './SchuelerEinwilligungenProps';
	import { SchuelerEinwilligungenModelProxy } from "./modelProxy/SchuelerEinwilligungenModelProxy";
	import type { SchuelerEinwilligung } from "@core/core/data/schueler/SchuelerEinwilligung";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useBenutzerState } from "@ui/states/BenutzerState";

	const props = defineProps<SchuelerEinwilligungenProps>();
	const benutzerState = useBenutzerState();

	const hatKompetenzAendern = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_EINWILLIGUNGEN_AENDERN));
	const readonly = computed(() => !hatKompetenzAendern.value);
	const noEntries = computed<boolean>(() => props.einwilligungen().isEmpty());
	const einwilligungenProxies = computed(() => {
		const result = new ArrayList<SchuelerEinwilligungenModelProxy>();
		for (const einwilligung of props.einwilligungen()) {
			const modelProxy = new SchuelerEinwilligungenModelProxy(() => einwilligung, (data: Partial<SchuelerEinwilligung>) => props.patch(data, einwilligung.idEinwilligungsart));
			result.add(modelProxy);
		}
		return result;
	});

	const hasAbgefragteEinwilligungen = computed(() => {
		for (const einwilligung of props.einwilligungen()) {
			if ((einwilligung.abgefragt || einwilligung.status)) {
				return true;
			}
		}
		return false;
	});

	const hasNichtAbgefragteEinwilligungen = computed(() => {
		for (const einwilligung of props.einwilligungen()) {
			if ((!einwilligung.abgefragt && !einwilligung.status)) {
				return true;
			}
		}
		return false;
	});

	function getBezeichnungEinwilligungsart(idEinwilligungsart: number): string {
		return props.mapEinwilligungsarten.get(idEinwilligungsart)?.bezeichnung ?? "";
	}

	function getEinwilligungsstatus(einwilligung: SchuelerEinwilligung): string {
		if (einwilligung.abgefragt && einwilligung.status) {
			return 'Abgefragt und Zugestimmt';
		} else if (einwilligung.status) {
			return 'Zugestimmt';
		} else if (einwilligung.abgefragt) {
			return 'Abgefragt';
		} else {
			return '';
		}
	}

</script>
