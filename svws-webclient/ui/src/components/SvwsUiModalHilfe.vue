<template>
	<div>
		<svws-ui-button type="secondary" @click="modal.openModal()">
			<i-ri-question-line />
			<span>Hilfe</span>
		</svws-ui-button>
		<svws-ui-modal ref="modal" size="help" class="hidden">
			<template #modalTitle>
				<div class="inline-flex items-center gap-1 mr-4">
					Hilfe <i-ri-question-line />
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

	const modal = ref();

</script>

<style lang="postcss">
.markdown-body {
	@apply prose leading-tight;

	* {
		@apply first:mt-0;
	}
}
</style>
