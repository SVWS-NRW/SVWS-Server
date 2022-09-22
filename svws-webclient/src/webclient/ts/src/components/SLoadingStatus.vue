<template>
	<div class="loading-status">
		<p>
			<img src="../../public/loading_spinner.svg" alt="Ladeanzeige" class="loading-spinner-dimensions loading-rotation">
		</p>
		<p>{{loading_message}}</p>
	</div>
</template>
<script setup lang="ts">
import { computed, ComputedRef } from "vue";
import { injectMainApp } from "~/apps/Main";

const app = injectMainApp();
const loading_message: ComputedRef<string> = computed(() => app.config.apiLoadingStatus.loading_message);
</script>
<style>
	.loading-status {
		@apply flex flex-row items-center;
		@apply space-x-4;
		@apply 2xl:flex-grow 2xl:justify-start;
	}

	.loading-spinner-dimensions {
		height: 1rem;
		width: 1rem;
	}

	.loading-rotation {
		display: block;
		position: relative;
		-webkit-animation: spin 6s steps(11, end) infinite;
		-moz-animation: spin 6s steps(11, end)  infinite;
		animation: spin 6s steps(11, end)  infinite;
	}

	@-webkit-keyframes spin{
		0%{-webkit-transform: rotate(0deg);}
		100%{-webkit-transform: rotate(360deg);}
	}
	@-moz-keyframes spin{
		0%{-webkit-transform: rotate(0deg);}
		100%{-webkit-transform: rotate(360deg);}
	}
	@keyframes spin{
		0%{-webkit-transform: rotate(0deg);}
		100%{-webkit-transform: rotate(360deg);}
	}
</style>
