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
				<slot>Für diesen Bereich wurde noch kein Text geschrieben.</slot>
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">

	import { ref } from 'vue';

	const show = ref<boolean>(false);

	defineSlots();

</script>
