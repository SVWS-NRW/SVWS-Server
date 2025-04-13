<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card v-if="hasAbgefragteEinwilligungen" title="Abgefragt" class="col-span-full">
			<div class="space-y-2">
				<div v-for="einwilligung of einwilligungen()" :key="einwilligung.idEinwilligungsart">
					<template v-if="einwilligung.abgefragt || einwilligung.status">
						<ui-card icon="i-ri-message-line" :title="getBezeichnungEinwilligungsart(einwilligung.idEinwilligungsart)" :info="getEinwilligungsstatus(einwilligung)">
							<div class="w-1/5">
								<p class="text-headline-md mb-1"> Status </p>
							</div>
							<svws-ui-checkbox class="w-2/5" :model-value="einwilligung.abgefragt" type="checkbox" title="Abgefragt"
								@update:model-value="abgefragt => patch({ abgefragt }, einwilligung.idEinwilligungsart)">
								Abgefragt
							</svws-ui-checkbox>
							<svws-ui-checkbox class="w-2/5" :model-value="einwilligung.status" type="checkbox" title="Zugestimmt"
								@update:model-value="status => patch({ status }, einwilligung.idEinwilligungsart)">
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
					<template v-if="!einwilligung.abgefragt && !einwilligung.status">
						<ui-card icon="i-ri-message-line" :title="getBezeichnungEinwilligungsart(einwilligung.idEinwilligungsart)">
							<div class="w-1/5">
								<p class="text-headline-md mb-1"> Status </p>
							</div>
							<svws-ui-checkbox class="w-2/5" :model-value="einwilligung.abgefragt" type="checkbox" title="Abgefragt"
								@update:model-value="abgefragt => patch({ abgefragt }, einwilligung.idEinwilligungsart)">
								Abgefragt
							</svws-ui-checkbox>
							<svws-ui-checkbox class="w-2/5" :model-value="einwilligung.status" type="checkbox" title="Zugestimmt"
								@update:model-value="status => patch({ status }, einwilligung.idEinwilligungsart)">
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
	import type { SchuelerEinwilligungenProps } from './SchuelerEinwilligungenProps';
	import type { SchuelerEinwilligung} from "@core";

	const props = defineProps<SchuelerEinwilligungenProps>();

	const hasAbgefragteEinwilligungen = computed(() => {
		for (const einwilligung of props.einwilligungen())
			if ((einwilligung.abgefragt || einwilligung.status))
				return true;
		return false;
	});

	const hasNichtAbgefragteEinwilligungen = computed(() => {
		for (const einwilligung of props.einwilligungen())
			if ((!einwilligung.abgefragt && !einwilligung.status))
				return true;
		return false;
	});

	function getBezeichnungEinwilligungsart(idEinwilligungsart: number): string {
		return props.mapEinwilligungsarten.get(idEinwilligungsart)?.bezeichnung ?? "";
	}

	function getEinwilligungsstatus(einwilligung: SchuelerEinwilligung): string {
		if (einwilligung.abgefragt && einwilligung.status)
			return 'Abgefragt und Zugestimmt';
		else if (einwilligung.status)
			return 'Zugestimmt';
		else if (einwilligung.abgefragt)
			return 'Abgefragt';
		else
			return '';
	}
</script>
