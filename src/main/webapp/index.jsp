<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UniRegis - Portail Académique Multi-Rôles</title>
    <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
</head>
<body class="bg-gray-100 font-sans antialiased">

<nav class="bg-[#525D76] text-white p-4 shadow-md">
    <div class="container mx-auto flex flex-col sm:flex-row justify-between items-center gap-2">
        <div>
            <h1 class="text-xl font-bold tracking-wide">UniRegis Portal</h1>
            <p class="text-xs text-gray-300">Système d'Inscription Distribué — ISB Mauritanie</p>
        </div>
        <div class="flex bg-[#414a5e] p-1 rounded-lg border border-gray-500">
            <button onclick="changerVue('etudiant')" id="btnTabEtudiant"
                    class="px-4 py-1.5 text-xs font-bold rounded-md transition-all bg-[#525D76] text-white shadow-sm cursor-pointer">
                🎓 Espace Étudiant
            </button>
            <button onclick="changerVue('admin')" id="btnTabAdmin"
                    class="px-4 py-1.5 text-xs font-bold rounded-md transition-all text-gray-300 hover:text-white cursor-pointer">
                🔑 Espace Administration
            </button>
        </div>
    </div>
</nav>

<div class="container mx-auto p-6 space-y-6">

    <div id="globalFeedback" class="p-3 rounded-lg text-xs font-medium hidden transition-all"></div>

    <div id="vueEtudiant" class="grid grid-cols-1 lg:grid-cols-3 gap-6">

        <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-200 h-fit">
            <h2 class="text-base font-bold text-gray-800 mb-4 border-b border-gray-100 pb-2 flex items-center gap-2">
                📝 Formulaire d'Inscription
            </h2>
            <form id="inscriptionForm" class="space-y-4">
                <div>
                    <label class="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">Nom</label>
                    <input type="text" id="nom" required class="w-full p-2 border border-gray-300 rounded-lg text-sm" placeholder="Ex: Diarra">
                </div>
                <div>
                    <label class="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">Prénom</label>
                    <input type="text" id="prenom" required class="w-full p-2 border border-gray-300 rounded-lg text-sm" placeholder="Ex: Ahmed">
                </div>
                <div>
                    <label class="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">Filière / Spécialité</label>
                    <select id="filiere" required class="w-full p-2 border border-gray-300 rounded-lg text-sm bg-white">
                        <option value="Génie Logiciel">Génie Logiciel (GL)</option>
                        <option value="Réseaux & Sécurité">Réseaux & Sécurité (RIT)</option>
                        <option value="Data Science">Data Science (DS)</option>
                    </select>
                </div>
                <button type="submit" class="w-full bg-[#525D76] text-white p-2.5 rounded-lg font-medium hover:bg-[#414a5e] transition-colors shadow-sm cursor-pointer">
                    Soumettre mon inscription
                </button>
            </form>
        </div>

        <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-200 lg:col-span-2">
            <h2 class="text-base font-bold text-gray-800 mb-4 border-b border-gray-100 pb-2">
                🔍 Suivre mon Statut en Temps Réel
            </h2>
            <p class="text-xs text-gray-500 mb-4">Entrez votre matricule pour vérifier l'état d'avancement de votre dossier.</p>
            <div class="flex gap-2 mb-6">
                <input type="text" id="rechercheMatricule" class="p-2 border border-gray-300 rounded-lg text-sm flex-1" placeholder="Entrez votre matricule exact (Ex: ISB-001)...">
                <button onclick="suivreStatutEtudiant()" class="bg-blue-600 text-white px-4 py-2 rounded-lg text-xs font-semibold hover:bg-blue-700 cursor-pointer">
                    Rechercher 🔎
                </button>
            </div>
            <div id="resultatSuivi" class="hidden border rounded-xl p-4 bg-gray-50">
            </div>
        </div>
    </div>

    <div id="vueAdmin" class="space-y-6 hidden">

        <div class="bg-amber-50 border border-amber-200 p-4 rounded-xl shadow-sm opacity-60">
            <h3 class="text-sm font-bold text-amber-900 mb-1 flex items-center gap-2">
                🔐 Sécurité RBAC & MicroProfile JWT (Contournée)
            </h3>
            <p class="text-xs text-amber-700">Le bouton utilise désormais l'URI ouverte sans filtre d'authentification pour faciliter la démo.</p>
        </div>

        <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-200">
            <div class="flex justify-between items-center mb-4 border-b border-gray-100 pb-2">
                <h2 class="text-base font-bold text-gray-800 flex items-center gap-2">
                    📋 Liste Générale des Demandes d'Inscriptions
                </h2>
                <button onclick="chargerInscriptionsAdmin()" class="text-xs bg-blue-600 text-white px-3 py-1.5 rounded-lg font-semibold hover:bg-blue-700 flex items-center gap-1 cursor-pointer">
                    <span>Rafraîchir la base</span> <span id="refreshSpinner">🔄</span>
                </button>
            </div>

            <div class="overflow-x-auto rounded-lg border border-gray-100">
                <table class="w-full text-left border-collapse">
                    <thead>
                    <tr class="bg-gray-50 text-gray-500 uppercase text-xs tracking-wider border-b border-gray-200">
                        <th class="p-3">ID</th>
                        <th class="p-3">Étudiant (Nom & Prénom)</th>
                        <th class="p-3">Matricule</th>
                        <th class="p-3">Filière</th>
                        <th class="p-3">Statut</th>
                        <th class="p-3 text-center">Actions Administrateur</th>
                    </tr>
                    </thead>
                    <tbody id="adminTableBody" class="text-sm text-gray-600 divide-y divide-gray-100">
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</div>

<script>
    const API_URL = '${pageContext.request.contextPath}/api/v1/inscriptions';

    // Basculement d'affichage (Tabs)
    function changerVue(role) {
        const vueE = document.getElementById('vueEtudiant');
        const vueA = document.getElementById('vueAdmin');
        const btnE = document.getElementById('btnTabEtudiant');
        const btnA = document.getElementById('btnTabAdmin');

        if (role === 'etudiant') {
            if(vueE) vueE.classList.remove('hidden');
            if(vueA) vueA.classList.add('hidden');
            if(btnE) btnE.className = "px-4 py-1.5 text-xs font-bold rounded-md bg-[#525D76] text-white shadow-sm cursor-pointer";
            if(btnA) btnA.className = "px-4 py-1.5 text-xs font-bold rounded-md text-gray-300 hover:text-white cursor-pointer";
        } else {
            if(vueE) vueE.classList.add('hidden');
            if(vueA) vueA.classList.remove('hidden');
            if(btnE) btnE.className = "px-4 py-1.5 text-xs font-bold rounded-md text-gray-300 hover:text-white cursor-pointer";
            if(btnA) btnA.className = "px-4 py-1.5 text-xs font-bold rounded-md bg-[#525D76] text-white shadow-sm cursor-pointer";
            chargerInscriptionsAdmin();
        }
    }

    // Notifications UI globales
    function notifier(message, isSuccess) {
        const el = document.getElementById('globalFeedback');
        if (el) {
            el.className = `p-3 rounded-lg text-xs font-medium block shadow-xs ` + (isSuccess ? 'bg-green-50 text-green-700 border border-green-200' : 'bg-red-50 text-red-700 border border-red-200');
            el.innerText = message;
            window.scrollTo({ top: 0, behavior: 'smooth' });
            setTimeout(() => el.classList.add('hidden'), 8000);
        }
    }

    // ================= SCRIPT : CÔTÉ ÉTUDIANT =================

    // POST : Soumettre le formulaire d'inscription (Matricule automatique côté serveur)
    const form = document.getElementById('inscriptionForm');
    if (form) {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();

            const payload = {
                nom: document.getElementById('nom').value.trim(),
                prenom: document.getElementById('prenom').value.trim(),
                filiere: document.getElementById('filiere').value
            };

            try {
                const response = await fetch(API_URL, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });

                if (response.status === 201 || response.status === 200) {
                    const dataGeneree = await response.json();
                    // Récupération dynamique du matricule calculé par InscriptionService
                    const matGenere = (dataGeneree.etudiant && dataGeneree.etudiant.matricule) ? dataGeneree.etudiant.matricule : "Généré";

                    notifier("Félicitations ! Votre demande d'inscription a bien été enregistrée. Notez précieusement votre matricule automatique : " + matGenere, true);
                    document.getElementById('inscriptionForm').reset();
                } else {
                    throw new Error("HTTP " + response.status);
                }
            } catch (error) {
                notifier("Erreur lors de la soumission de la demande : " + error.message, false);
            }
        });
    }

    // GET + Filtre : Suivre l'état d'une inscription par son matricule
    async function suivreStatutEtudiant() {
        const inputMat = document.getElementById('rechercheMatricule').value.trim();
        const container = document.getElementById('resultatSuivi');

        if(!inputMat) { alert("Veuillez saisir votre matricule."); return; }

        try {
            const response = await fetch(API_URL);
            if (!response.ok) throw new Error();
            const list = await response.json();

            const trouve = list.find(ins => ins.etudiant && ins.etudiant.matricule && ins.etudiant.matricule.toLowerCase() === inputMat.toLowerCase());

            container.classList.remove('hidden');
            if (trouve) {
                const badge = trouve.statut === 'VALIDE' ? 'bg-green-100 text-green-800 border border-green-200' : 'bg-yellow-100 text-yellow-800 border border-yellow-200';
                container.innerHTML = `
                    <div class="flex justify-between items-center">
                        <div>
                            <h4 class="text-sm font-bold text-gray-900">` + trouve.etudiant.prenom + ` ` + trouve.etudiant.nom + `</h4>
                            <p class="text-xs text-gray-600 mt-0.5">Filière : <strong>` + trouve.etudiant.filiere + `</strong></p>
                            <p class="text-xs text-gray-400 mt-2 font-mono">Dossier Référence : #` + trouve.id + `</p>
                        </div>
                        <span class="px-3 py-1 rounded-full text-xs font-bold ` + badge + `">` + trouve.statut + `</span>
                    </div>`;
            } else {
                container.innerHTML = `<p class="text-xs text-red-500 italic">Aucun dossier trouvé pour le matricule "` + inputMat + `".</p>`;
            }
        } catch (e) {
            container.innerHTML = `<p class="text-xs text-red-500">Erreur lors de la communication avec le service JAX-RS.</p>`;
        }
    }


    // ================= SCRIPT : CÔTÉ ADMINISTRATION =================

    // GET : Lister l'ensemble des inscriptions (Espace Admin)
    async function chargerInscriptionsAdmin() {
        const spinner = document.getElementById('refreshSpinner');
        const tbody = document.getElementById('adminTableBody');

        try {
            if (spinner) spinner.classList.add('animate-spin');
            const response = await fetch(API_URL, { method: 'GET' });
            if (!response.ok) throw new Error("Erreur HTTP " + response.status);

            const data = await response.json();
            tbody.innerHTML = '';

            if (data.length === 0) {
                tbody.innerHTML = '<tr><td colspan="6" class="p-4 text-center text-gray-400 italic">Aucune inscription présente dans la base PostgreSQL.</td></tr>';
                return;
            }

            data.forEach(ins => {
                const tr = document.createElement('tr');
                tr.className = "hover:bg-gray-50/70 transition-colors";

                const isValide = ins.statut === 'VALIDE';
                const badgeStyle = isValide ? 'bg-emerald-100 text-emerald-800' : 'bg-amber-100 text-amber-800';

                let actionButton = isValide
                    ? '<span class="text-emerald-600 text-xs font-semibold flex items-center justify-center gap-1">Validé ✅</span>'
                    : '<button onclick="validerDossierAdmin(' + ins.id + ')" class="bg-emerald-600 text-white px-3 py-1 rounded-md text-xs font-medium hover:bg-emerald-700 transition-colors cursor-pointer shadow-xs">Valider l\'inscription (PUT)</button>';

                const prenom = ins.etudiant ? ins.etudiant.prenom : '';
                const nom = ins.etudiant ? ins.etudiant.nom : 'Inconnu';
                const matricule = ins.etudiant ? ins.etudiant.matricule : 'N/A';
                const filiere = ins.etudiant ? ins.etudiant.filiere : 'Non spécifiée';

                tr.innerHTML = `
                    <td class="p-3 font-mono font-bold text-gray-700">#` + ins.id + `</td>
                    <td class="p-3 font-semibold text-gray-900">` + prenom + ` ` + nom + `</td>
                    <td class="p-3 font-mono text-xs text-gray-600">` + matricule + `</td>
                    <td class="p-3 text-xs text-gray-600">` + filiere + `</td>
                    <td class="p-3"><span class="px-2.5 py-0.5 rounded text-xs font-bold ` + badgeStyle + `">` + ins.statut + `</span></td>
                    <td class="p-3 text-center">` + actionButton + `</td>`;

                if(tbody) tbody.appendChild(tr);
            });
        } catch (error) {
            if(tbody) tbody.innerHTML = '<tr><td colspan="6" class="p-4 text-center text-red-500 font-semibold">Échec de synchronisation JAX-RS : ' + error.message + '</td></tr>';
        } finally {
            if (spinner) spinner.classList.remove('animate-spin');
        }
    }

    // PUT : Modification directe via la route ouverte sans filtre JWT
    async function validerDossierAdmin(id) {
        try {
            const response = await fetch(API_URL + '/' + id + '/changer-statut', {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ statut: 'VALIDE' })
            });

            if (response.ok) {
                notifier("Le dossier #" + id + " a été validé avec succès en base de données !", true);
                chargerInscriptionsAdmin();
            } else {
                throw new Error("Erreur de traitement backend (" + response.status + ")");
            }
        } catch (error) {
            notifier("Impossible de traiter la demande : " + error.message, false);
        }
    }

    // Initialisation par défaut
    window.onload = function() {
        changerVue('etudiant');
    };
</script>
</body>
</html>
