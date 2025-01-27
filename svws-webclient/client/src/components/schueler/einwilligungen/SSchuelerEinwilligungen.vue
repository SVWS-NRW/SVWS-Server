<template>
	<div class="page--content">
		<svws-ui-content-card title="" class="col-span-full">
			<svws-ui-input-wrapper class="w-5/5 min-h-10">
				<div class="w-full flex justify-end">
					<div class="w-1/3 ml-auto mr-0 p-3 min-h-10">
						<svws-ui-select v-model="auswahlEinwilligungsartNeu" label="Bitte eine Einwilligungsart auswählen" :items="filteredEinwilligungsarten"
							:item-text="(i) => i.bezeichnung" />
					</div>
					<div class="p-3 min-h-10">
						<svws-ui-button class="ml-auto p-3 min-h-10" :disabled="!auswahlEinwilligungsartNeu" @click="auswahlEinwilligungsartNeu?.id !== undefined && add(auswahlEinwilligungsartNeu?.id)">
							<p class="mr-4">Neue Einwilligung hinzufügen</p>
							<span class="icon icon-lg i-ri-chat-new-line" />
						</svws-ui-button>
					</div>
				</div>
			</svws-ui-input-wrapper>
			<div v-for="einwilligung of einwilligungen()" :key="einwilligung.idEinwilligungsart">
				<svws-ui-card icon="i-ri-message-line" :title="getBezeichnungEinwilligungsart(einwilligung.idEinwilligungsart)"
					:info="getEinwilligungsstatus(einwilligung)">
					<template #content>
						<div class="w-1/5">
							<p class="text-headline-md mb-1"> Status </p>
						</div>
						<svws-ui-checkbox class="w-2/5" :model-value="einwilligung.abgefragt" type="checkbox" title="Abgefragt"
							@update:model-value="(updated) => patch( { abgefragt: updated }, einwilligung.idEinwilligungsart)">
							Abgefragt
						</svws-ui-checkbox>
						<svws-ui-checkbox class="w-2/5" :model-value="einwilligung.status" type="checkbox" title="Zugestimmt"
							@update:model-value="(updated) => patch({ status: updated }, einwilligung.idEinwilligungsart)">
							Zugestimmt
						</svws-ui-checkbox>
					</template>
					<template #buttonContentRight>
						<svws-ui-button	type="danger" class="deleteButton" @click="remove(einwilligung.idEinwilligungsart)">
							Löschen
						</svws-ui-button>
					</template>
				</svws-ui-card>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerEinwilligungenProps } from './SchuelerEinwilligungenProps';
	import type { Einwilligungsart, Einwilligung } from "@core";
	import { ref, computed } from "vue";

	const props = defineProps<SchuelerEinwilligungenProps>();

	const auswahlEinwilligungsartNeu = ref<Einwilligungsart>();

	function getBezeichnungEinwilligungsart(idEinwilligungsart: number): string {
		return props.mapEinwilligungsarten.get(idEinwilligungsart)?.bezeichnung ?? "";
	}

	function getEinwilligungsstatus(einwilligung: Einwilligung): string {
		if (einwilligung.abgefragt && einwilligung.status)
			return 'Abgefragt und Zugestimmt';
		else if (einwilligung.status) {
			return 'Zugestimmt';
		} else if (einwilligung.abgefragt) {
			return 'Abgefragt';
		} else {
			return '';
		}
	}

	const filteredEinwilligungsarten = computed(() => {
		const verwendeteEinwilligungsarten = [...props.einwilligungen()].map((e) => e.idEinwilligungsart )
		return props.mapEinwilligungsarten.values().filter(
			(einwilligungsart) => !verwendeteEinwilligungsarten.includes(einwilligungsart.id)
		);
	});

</script>
