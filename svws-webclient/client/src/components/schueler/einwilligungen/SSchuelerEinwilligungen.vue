<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card v-if="einwilligungen().size() > 0" title="Zugeordnete Einwilligungen" class="col-span-full">
			<div class="space-y-2">
				<div v-for="einwilligung of einwilligungen()" :key="einwilligung.idEinwilligungsart">
					<ui-card icon="i-ri-message-line" :title="getBezeichnungEinwilligungsart(einwilligung.idEinwilligungsart)" :info="getEinwilligungsstatus(einwilligung)">
						<div class="w-1/5">
							<p class="text-headline-md mb-1"> Status </p>
						</div>
						<svws-ui-checkbox class="w-2/5" :model-value="einwilligung.abgefragt" type="checkbox" title="Abgefragt"
							@update:model-value="abgefragt => patch( { abgefragt }, einwilligung.idEinwilligungsart)">
							Abgefragt
						</svws-ui-checkbox>
						<svws-ui-checkbox class="w-2/5" :model-value="einwilligung.status" type="checkbox" title="Zugestimmt"
							@update:model-value="status => patch({ status }, einwilligung.idEinwilligungsart)">
							Zugestimmt
						</svws-ui-checkbox>
					</ui-card>
				</div>
			</div>
		</svws-ui-content-card>
		<svws-ui-content-card v-if="filteredEinwilligungsarten.length > 0" title="Nicht abgefragt" class="col-span-full">
			<div class="space-y-2">
				<div v-for="einwilligungsart of filteredEinwilligungsarten" :key="einwilligungsart">
					<ui-card icon="i-ri-message-line" :title="getBezeichnungEinwilligungsart(einwilligungsart)">
						<div class="w-1/5">
							<p class="text-headline-md mb-1"> Status </p>
						</div>
						<svws-ui-checkbox class="w-2/5" :model-value="false" type="checkbox" title="Abgefragt"
							@update:model-value="addEinwilligung( einwilligungsart, true, false )">
							Abgefragt
						</svws-ui-checkbox>
						<svws-ui-checkbox class="w-2/5" :model-value="false" type="checkbox" title="Zugestimmt"
							@update:model-value="addEinwilligung(einwilligungsart, true, true)">
							Zugestimmt
						</svws-ui-checkbox>
					</ui-card>
				</div>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">


	import { computed } from "vue";
	import type { SchuelerEinwilligungenProps } from './SchuelerEinwilligungenProps';
	import type { Einwilligung } from "@core";
	import { PersonTyp } from "@core";

	const props = defineProps<SchuelerEinwilligungenProps>();

	const filteredEinwilligungsarten = computed(() => {
		const verwendeteEinwilligungsarten = new Set<number>();
		for (const einwilligung of props.einwilligungen())
			verwendeteEinwilligungsarten.add(einwilligung.idEinwilligungsart);
		const result = [];
		for (const einwilligungsart of props.mapEinwilligungsarten.values())
			if ((!verwendeteEinwilligungsarten.has(einwilligungsart.id)) && (einwilligungsart.personTyp === PersonTyp.SCHUELER.id))
				result.push(einwilligungsart.id);
		return result;
	});

	async function addEinwilligung(idEinwilligungsart: number, abgefragt: boolean, status: boolean) {
		const einwilligungNeu: Partial<Einwilligung> = { idEinwilligungsart, abgefragt, status };
		await props.add(einwilligungNeu, idEinwilligungsart);
	}

	function getBezeichnungEinwilligungsart(idEinwilligungsart: number): string {
		return props.mapEinwilligungsarten.get(idEinwilligungsart)?.bezeichnung ?? "";
	}

	function getEinwilligungsstatus(einwilligung: Einwilligung): string {
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
