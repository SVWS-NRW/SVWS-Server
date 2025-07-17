<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-lehrer-einwilligungen /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-grid-cards">
		<svws-ui-content-card v-if="hasAbgefragteEinwilligungen" title="Abgefragt" class="col-span-full">
			<div class="space-y-2">
				<div v-for="einwilligung of einwilligungen()" :key="einwilligung.idEinwilligungsart">
					<template v-if="einwilligung.istAbgefragt || einwilligung.istZugestimmt">
						<ui-card icon="i-ri-message-line" :title="getBezeichnungEinwilligungsart(einwilligung.idEinwilligungsart)" :info="getEinwilligungsstatus(einwilligung)">
							<div class="w-1/5">
								<p class="text-headline-md mb-1"> Status </p>
							</div>
							<svws-ui-checkbox class="w-2/5" :model-value="einwilligung.istAbgefragt" type="checkbox" title="Abgefragt"
								@update:model-value="istAbgefragt => patch({ istAbgefragt }, einwilligung.idEinwilligungsart)">
								Abgefragt
							</svws-ui-checkbox>
							<svws-ui-checkbox class="w-2/5" :model-value="einwilligung.istZugestimmt" type="checkbox" title="Zugestimmt"
								@update:model-value="istZugestimmt => patch({ istZugestimmt }, einwilligung.idEinwilligungsart)">
								Zugestimmt
							</svws-ui-checkbox>
						</ui-card>
					</template>
				</div>
			</div>
		</svws-ui-content-card>
		<svws-ui-content-card v-if="hasNichtAbgefragteEinwilligungen" title="Nicht abgefragt" class="col-span-full">
			<div class="space-y-2">
				<div v-for="einwilligung of einwilligungen()" :key="einwilligung.idEinwilligungsart">
					<template v-if="!einwilligung.istAbgefragt && !einwilligung.istZugestimmt">
						<ui-card icon="i-ri-message-line" :title="getBezeichnungEinwilligungsart(einwilligung.idEinwilligungsart)">
							<div class="w-1/5">
								<p class="text-headline-md mb-1"> Status </p>
							</div>
							<svws-ui-checkbox class="w-2/5" :model-value="einwilligung.istAbgefragt" type="checkbox" title="Abgefragt"
								@update:model-value="istAbgefragt => patch({ istAbgefragt }, einwilligung.idEinwilligungsart)">
								Abgefragt
							</svws-ui-checkbox>
							<svws-ui-checkbox class="w-2/5" :model-value="einwilligung.istZugestimmt" type="checkbox" title="Zugestimmt"
								@update:model-value="istZugestimmt => updateEinwilligungStatus(einwilligung, istZugestimmt)">
								Zugestimmt
							</svws-ui-checkbox>
						</ui-card>
					</template>
				</div>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import { computed } from "vue";
	import type { LehrerEinwilligungenProps } from './LehrerEinwilligungenProps';
	import type { LehrerEinwilligung } from "@core";

	const props = defineProps<LehrerEinwilligungenProps>();

	const hasAbgefragteEinwilligungen = computed(() => {
		for (const einwilligung of props.einwilligungen())
			if ((einwilligung.istAbgefragt || einwilligung.istZugestimmt))
				return true;
		return false;
	});

	const hasNichtAbgefragteEinwilligungen = computed(() => {
		for (const einwilligung of props.einwilligungen())
			if ((!einwilligung.istAbgefragt && !einwilligung.istZugestimmt))
				return true;
		return false;
	});

	function getBezeichnungEinwilligungsart(idEinwilligungsart: number): string {
		return props.mapEinwilligungsarten.get(idEinwilligungsart)?.bezeichnung ?? "";
	}

	async function updateEinwilligungStatus(einwilligung: LehrerEinwilligung, istZugestimmt: boolean) {
		const update: Partial<LehrerEinwilligung> = { istZugestimmt };
		if ((istZugestimmt) && (!einwilligung.istAbgefragt))
			update.istAbgefragt = true;
		await props.patch(update, einwilligung.idEinwilligungsart);
	}

	function getEinwilligungsstatus(einwilligung: LehrerEinwilligung): string {
		if (einwilligung.istAbgefragt && einwilligung.istZugestimmt)
			return 'Abgefragt und Zugestimmt';
		else if (einwilligung.istZugestimmt)
			return 'Zugestimmt';
		else if (einwilligung.istAbgefragt)
			return 'Abgefragt';
		else
			return '';
	}
</script>
