<template>
	<div>
		<svws-ui-button type="secondary" @click="show = true">
			<span class="icon i-ri-question-line" />
			<span>Hilfe</span>
		</svws-ui-button>
		<svws-ui-modal v-model:show="show" size="help" class="hidden">
			<template #modalTitle>
				<div class="inline-flex items-center gap-1 mr-4">
					Hilfe <span class="icon i-ri-question-line" />
				</div>
			</template>
			<template #modalDescription>
				<div class="markdown">
					<slot>Für diesen Bereich wurde noch kein Text geschrieben.</slot>
				</div>
				<template v-if="!disabledFooter">
					<hr class="border-2 my-4">Nutzen Sie für weitere Informationen und Anleitungen die <a class="text-ui-brand" href="https://doku.svws-nrw.de" target="blank">ausführliche Dokumentation</a>.
				</template>
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">

	import { ref } from 'vue';

	const props = defineProps<{
		disabledFooter?: boolean;
	}>();

	const show = ref<boolean>(false);

	defineSlots();

</script>
