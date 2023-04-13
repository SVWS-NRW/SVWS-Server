<template>
	<tr v-if="apiStatus.hasError || apiStatus.pending">
		<template v-if="apiStatus.pending">
			<td class="loading-disclaimer">
				<div class="loading-display">
					<span>Blockung wird berechnet...</span>
					<span class="loading-spinner-dimensions">
						<img src="/loading_spinner.svg"
							alt="Ladeanzeige"
							class="loading-spinner-dimensions loading-rotation">
					</span>
				</div>
			</td>
		</template>
		<template v-if="apiStatus.hasError">
			<td>
				<p class="api-error-text">Fehler beim Berechnen der Blockung!</p>
			</td>
		</template>
	</tr>
</template>

<script setup lang="ts">

	import type { GostBlockungListeneintrag } from "@svws-nrw/svws-core";
	import type { ApiStatus } from "~/components/ApiStatus";

	const props = defineProps<{
		blockung: GostBlockungListeneintrag;
		apiStatus: ApiStatus;
	}>();

</script>

<style>
	.loading-disclaimer {
		background-color: rgba(var(--svws-ui-color-dark-20), var(--tw-border-opacity));
		--tw-bg-opacity: 1;
		--tw-border-opacity: 1;
		border-width: 1px;
		padding: 1rem .75rem;
		line-height: 1.125;
	}

	.loading-spinner-dimensions {
		height: 1rem;
		width: 1rem;
	}

	.loading-display {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
	}

	.loading-rotation {
		display: block;
		position: relative;
		-webkit-animation: spin 6s steps(11, end) infinite;
		-moz-animation: spin 6s steps(11, end) infinite;
		animation: spin 6s steps(11, end) infinite;
	}

	.api-error-text {
		color: rgb(var(--svws-ui-color-error));
		font-weight: 700;
	}

	@-webkit-keyframes spin {
		0% {
			-webkit-transform: rotate(0deg);
		}

		100% {
			-webkit-transform: rotate(360deg);
		}
	}

	@-moz-keyframes spin {
		0% {
			-webkit-transform: rotate(0deg);
		}

		100% {
			-webkit-transform: rotate(360deg);
		}
	}

	@keyframes spin {
		0% {
			-webkit-transform: rotate(0deg);
		}

		100% {
			-webkit-transform: rotate(360deg);
		}
	}
</style>