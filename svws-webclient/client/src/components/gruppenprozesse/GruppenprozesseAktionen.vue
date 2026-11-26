<template>
	<div class="flex gap-4 pt-8 px-6 lg:px-9 3xl:px-12 4xl:px-20">
		<SvwsUiTooltip color="light" :show-arrow="false">
			<template #content>
				<template v-if="gruppenprozesse.isPatchAllowed">
					Änderungen übernehmen
				</template>
				<template v-else>
					Speichern nicht möglich:
					<div v-for="hinweis of gruppenprozesse.patchHinweise" :key="hinweis">{{ hinweis }}</div>
				</template>
			</template>
			<svws-ui-button :disabled="!gruppenprozesse.hasPending || !gruppenprozesse.isPatchAllowed || disableSave" @click="speichern" type="primary" size="big">
				Speichern
				<svws-ui-spinner :spinning="loading" />
			</svws-ui-button>
		</SvwsUiTooltip>
		<SvwsUiTooltip color="light" :show-arrow="false">
			<template #content>
				Änderungen zurücksetzen
			</template>
			<svws-ui-button :disabled="!gruppenprozesse.hasPending" @click="gruppenprozesse.clearPending()" type="danger" size="big">
				Zurücksetzen
			</svws-ui-button>
		</SvwsUiTooltip>
	</div>
</template>

<script setup lang="ts">

	import { ref } from "vue";

	import type { GenericGruppenprozessState } from "~/states/GenericGruppenprozessState";

	const props = withDefaults(defineProps<{
		/** Der Orchestrator der Gruppenprozesse, dessen ausstehende Änderungen aller Bereiche gespeichert oder verworfen werden */
		gruppenprozesse: GenericGruppenprozessState<any>;
		/** Sperrt das Speichern, z.B. bei ungültigen Eingaben */
		disableSave?: boolean;
	}>(), {
		disableSave: false,
	});

	const loading = ref<boolean>(false);

	async function speichern() {
		loading.value = true;
		try {
			await props.gruppenprozesse.patchMultiple();
		} finally {
			loading.value = false;
		}
	}

</script>
