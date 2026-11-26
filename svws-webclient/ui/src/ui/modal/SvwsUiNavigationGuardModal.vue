<template>
	<svws-ui-modal :show="query.isOpen" @update:show="onUpdateShow" :auto-close="false" :close-in-title="false" size="small" type="danger">
		<template #modalTitle><slot name="title">Ungespeicherte Änderungen</slot></template>
		<template #modalDescription>
			<div class="text-left">
				<slot name="description">
					Es gibt Änderungen, die noch nicht gespeichert wurden.<br>
					Wie möchten Sie fortfahren?
				</slot>
				<div v-if="query.info.bereiche.length > 0" class="mt-4">
					Änderungen in: {{ query.info.bereiche.join(", ") }}
				</div>
				<div v-if="!query.info.isPatchAllowed && (query.info.patchHinweise.length > 0)" class="mt-4 text-ui-danger">
					Speichern nicht möglich: {{ query.info.patchHinweise.join("; ") }}
				</div>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="query.reply('cancel')">Abbrechen</svws-ui-button>
			<svws-ui-button type="danger" @click="query.reply('discard')">Verwerfen und fortfahren</svws-ui-button>
			<svws-ui-button v-if="speichern" type="primary" :disabled="!query.info.isPatchAllowed" @click="query.reply('save')">Speichern und fortfahren</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { NavigationGuardQuery } from "./NavigationGuardQuery";

	const props = withDefaults(defineProps<{
		/** Die Abfrage des Guards, die das Modal anzeigt (mit den Angaben des Guards) und beantwortet */
		query: NavigationGuardQuery;
		/** Gibt an, ob "Speichern und fortfahren" angeboten wird */
		speichern?: boolean;
	}>(), {
		speichern: true,
	});

	defineSlots<{
		title?: () => unknown;
		description?: () => unknown;
	}>();

	/** Ein Schließen des Modals auf anderem Weg als über die Knöpfe gilt als Abbrechen */
	function onUpdateShow(show: boolean) {
		if (!show) {
			props.query.reply("cancel");
		}
	}

</script>
