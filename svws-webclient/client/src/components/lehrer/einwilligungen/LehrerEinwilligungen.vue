<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-lehrer-einwilligungen /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-grid-cards">
		<svws-ui-input-wrapper :grid="1">
			<div v-if="noEntries">
				Aktuell gibt es keine Einträge im Katalog "Einwilligungsarten".
			</div>
			<svws-ui-content-card v-if="hasAbgefragteEinwilligungen" title="Abgefragt" class="col-span-full">
				<div class="space-y-2">
					<div v-for="einwilligung of einwilligungenProxies" :key="einwilligung.proxy.idEinwilligungsart">
						<template v-if="einwilligung.proxy.istAbgefragt || einwilligung.proxy.istZugestimmt">
							<ui-card icon="i-ri-message-line" :title="getBezeichnungEinwilligungsart(einwilligung.proxy.idEinwilligungsart)" :info="getEinwilligungsstatus(einwilligung.proxy)">
								<div class="w-1/5">
									<p class="text-headline-md mb-1"> Status </p>
								</div>
								<svws-ui-checkbox class="w-2/5" v-model="einwilligung.currentAbgefragt.value" :readonly>
									Abgefragt
								</svws-ui-checkbox>
								<svws-ui-checkbox class="w-2/5" v-model="einwilligung.currentZugestimmt.value" :readonly>
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
						<template v-if="!einwilligung.proxy.istAbgefragt && !einwilligung.proxy.istZugestimmt">
							<ui-card icon="i-ri-message-line" :title="getBezeichnungEinwilligungsart(einwilligung.proxy.idEinwilligungsart)">
								<div class="w-1/5">
									<p class="text-headline-md mb-1"> Status </p>
								</div>
								<svws-ui-checkbox class="w-2/5" v-model="einwilligung.currentAbgefragt.value" :readonly>
									Abgefragt
								</svws-ui-checkbox>
								<svws-ui-checkbox class="w-2/5" v-model="einwilligung.currentZugestimmt.value" :readonly>
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

	import type { LehrerEinwilligung } from "@core/core/data/lehrer/LehrerEinwilligung";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useModelProxyList } from "@ui/model/useModelProxyList";
	import { useBenutzerState } from "@ui/states/BenutzerState";

	import { LehrerEinwilligungenModelProxy } from "~/components/lehrer/einwilligungen/modelproxy/LehrerEinwilligungenModelProxy";

	import type { LehrerEinwilligungenProps } from './LehrerEinwilligungenProps';

	const props = defineProps<LehrerEinwilligungenProps>();
	const benutzerState = useBenutzerState();

	const hatKompetenzAendern = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN));
	const readonly = computed(() => !hatKompetenzAendern.value);
	const noEntries = computed<boolean>(() => props.einwilligungen().isEmpty());

	const einwilligungenProxies = useModelProxyList(
		() => props.einwilligungen(),
		(lehrereinwilligung) => lehrereinwilligung.idEinwilligungsart,
		(lehrereinwilligung) => new LehrerEinwilligungenModelProxy(() => lehrereinwilligung,
			(data: Partial<LehrerEinwilligung>) => props.patch(data, lehrereinwilligung.idEinwilligungsart))
	);

	const hasAbgefragteEinwilligungen = computed(() => {
		for (const einwilligung of props.einwilligungen()) {
			if ((einwilligung.istAbgefragt || einwilligung.istZugestimmt)) {
				return true;
			}
		}
		return false;
	});

	const hasNichtAbgefragteEinwilligungen = computed(() => {
		for (const einwilligung of props.einwilligungen()) {
			if ((!einwilligung.istAbgefragt && !einwilligung.istZugestimmt)) {
				return true;
			}
		}
		return false;
	});

	function getBezeichnungEinwilligungsart(idEinwilligungsart: number): string {
		return props.mapEinwilligungsarten.get(idEinwilligungsart)?.bezeichnung ?? "";
	}

	function getEinwilligungsstatus(einwilligung: LehrerEinwilligung): string {
		if (einwilligung.istAbgefragt && einwilligung.istZugestimmt) {
			return 'Abgefragt und Zugestimmt';
		} else if (einwilligung.istZugestimmt) {
			return 'Zugestimmt';
		} else if (einwilligung.istAbgefragt) {
			return 'Abgefragt';
		} else {
			return '';
		}
	}
</script>
